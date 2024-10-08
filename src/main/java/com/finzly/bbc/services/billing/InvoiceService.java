package com.finzly.bbc.services.billing;

import com.finzly.bbc.constants.ConnectionStatus;
import com.finzly.bbc.constants.InvoiceTransactionStatus;
import com.finzly.bbc.constants.PaymentType;
import com.finzly.bbc.dtos.billing.*;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ConflictException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.billing.*;
import com.finzly.bbc.models.payment.Transaction;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.billing.ConnectionRepository;
import com.finzly.bbc.repositories.billing.InvoiceRepository;
import com.finzly.bbc.repositories.billing.PaymentTransactionRepository;
import com.finzly.bbc.repositories.billing.TariffRepository;
import com.finzly.bbc.services.notification.EmailService;
import com.finzly.bbc.services.payment.UpiService;
import com.finzly.bbc.utils.CsvParserUtil;
import com.finzly.bbc.utils.PdfGenerationUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private PdfGenerationUtility pdfGenerationUtility;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UpiService upiService;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    @Value("${finzly.upiId}")
    private String receiverUpiId;

    // Method to create a single invoice
    public InvoiceResponse createInvoice (InvoiceRequest invoiceRequest) {
        validateInvoiceRequest (invoiceRequest);

        Customer customer = customerRepository.findById (invoiceRequest.getCustomerId ())
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found"));

        Connection connection = connectionRepository.findByCustomer (customer)
                .orElseThrow (() -> new ResourceNotFoundException ("Connection not found for customer"));

        if (!connection.getStatus ().equals (ConnectionStatus.ACTIVE)) {
            throw new ResourceNotFoundException ("Connection is not active");
        }

        // Check if an invoice already exists for the current month
        boolean invoiceExists = invoiceRepository.existsByConnectionAndMonth (connection, invoiceRequest.getMonth ().withDayOfMonth (1));
        if (invoiceExists) {
            throw new ConflictException ("Invoice already exists for this month for customer " + invoiceRequest.getCustomerId ());
        }

        // Find applicable tariff based on connection type and units
        Tariff applicableTariff = findApplicableTariff (connection.getConnectionType (), invoiceRequest.getUnits ());
        var invoice = getInvoice (invoiceRequest, applicableTariff, connection);

        Invoice savedInvoice = invoiceRepository.save (invoice);
        log.info ("Successfully created invoice for customer: {}", invoiceRequest.getCustomerId ());

        return mapToResponse (savedInvoice, applicableTariff, customer);
    }

    // Method to create multiple invoices (bulk)
    public BulkInvoiceResponse addBulkInvoices (BulkInvoiceRequest bulkInvoiceRequest) {
        List<InvoiceRequest> invoiceRequests = bulkInvoiceRequest.getInvoices ();
        List<InvoiceResponse> successfulResponses = new ArrayList<> ();
        List<FailedInvoiceRequest> failedRequests = new ArrayList<> ();

        for (InvoiceRequest invoiceRequest : invoiceRequests) {
            try {
                validateInvoiceRequest (invoiceRequest);
                InvoiceResponse response = createInvoice (invoiceRequest);
                successfulResponses.add (response);
                sendInvoice (response.getId ());
            } catch (Exception e) {
                log.error ("Failed to add invoice for request {}: {}", invoiceRequest, e.getMessage ());
                failedRequests.add (new FailedInvoiceRequest (invoiceRequest, e.getMessage ()));
            }
        }

        return BulkInvoiceResponse.builder ()
                .successfulResponses (successfulResponses)
                .failedRequests (failedRequests)
                .build ();
    }

    private void sendInvoice (String id) throws Exception {
        Invoice invoice = invoiceRepository.findById (id).orElseThrow (() -> new ResourceNotFoundException ("Invoice not found"));
        InvoiceResponse invoiceResponse = getInvoiceById (id);
        pdfGenerationUtility.generateInvoicePdf (invoiceResponse);

        // Send email
        emailService.sendInvoiceEmail (invoiceResponse);
    }

    public BulkInvoiceResponse addBulkInvoicesWithCsv (MultipartFile csvFile) {
        try {
            List<InvoiceRequest> invoiceRequests = CsvParserUtil.parseCsvFile (csvFile, this::mapCsvRecordToInvoiceRequest);

            BulkInvoiceRequest bulkRequest = new BulkInvoiceRequest ();
            bulkRequest.setInvoices (invoiceRequests);

            return addBulkInvoices (bulkRequest);

        } catch (IOException e) {
            log.error ("Error processing CSV file: {}", e.getMessage ());
            throw new BadRequestException ("Failed to process the CSV file.");
        }
    }

    private InvoiceRequest mapCsvRecordToInvoiceRequest (CSVRecord record) {
        return InvoiceRequest.builder ()
                .customerId (record.get ("customerId"))
                .dueDate (LocalDate.parse (record.get ("dueDate")))
                .month (LocalDate.parse (record.get ("month")))
                .units (Integer.valueOf (record.get ("units")))
                .build ();
    }

    // Method to get a single invoice by ID
    public InvoiceResponse getInvoiceById (String id) {
        Invoice invoice = invoiceRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException ("Invoice not found"));

        Customer customer = invoice.getConnection ().getCustomer ();
        Tariff applicableTariff = findApplicableTariff (invoice.getConnection ().getConnectionType (), invoice.getUnits ());

        return mapToResponse (invoice, applicableTariff, customer);
    }

    // Method to get all invoices
    public List<InvoiceResponse> getAllInvoicesForCustomer (
            String customerId,
            InvoiceTransactionStatus paymentStatus,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Customer customer = customerRepository.findById (customerId)
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found"));

        return invoiceRepository.findByConnection_Customer (customer)
                .stream ()
                .filter (invoice -> (paymentStatus == null || invoice.getPaymentStatus () == paymentStatus)) // Filter by payment status
                .filter (invoice -> (startDate == null || !invoice.getDueDate ().isBefore (startDate))) // Filter by start date
                .filter (invoice -> (endDate == null || !invoice.getDueDate ().isAfter (endDate))) // Filter by end date
                .map (invoice -> {
                    Tariff applicableTariff = findApplicableTariff (invoice.getConnection ().getConnectionType (), invoice.getUnits ());
                    return mapToResponse (invoice, applicableTariff, customer);
                })
                .sorted (Comparator.comparing (InvoiceResponse::getDueDate))
                .sorted (Comparator.comparing (InvoiceResponse::getPaymentStatus))
                .collect (Collectors.toList ());
    }


    public List<InvoiceResponse> getAllInvoicesForAllCustomers () {
        List<Invoice> invoices = invoiceRepository.findAll ();
        return invoices.stream ()
                .map (invoice -> {
                    Customer customer = invoice.getConnection ().getCustomer ();
                    Tariff applicableTariff = findApplicableTariff (invoice.getConnection ().getConnectionType (), invoice.getUnits ());
                    return mapToResponse (invoice, applicableTariff, customer);
                })
                .collect (Collectors.toList ());
    }


    public Transaction payByUpi (PayInvoiceByUpi invoiceByUpi) {

        Invoice invoice = invoiceRepository.findById (invoiceByUpi.getInvoiceId ())
                .orElseThrow (() -> new ResourceNotFoundException ("Invoice not found"));

        if (invoice.getPaymentStatus ().equals (InvoiceTransactionStatus.PAID)) {
            throw new BadRequestException ("Invoice has already been paid");
        }

        Double amount = invoice.getBillAmount ();

        if (invoice.getDueDate ().isAfter (LocalDate.now ())) {
            amount = amount * 0.95;
        }

        Transaction transaction = upiService.payByUPI (invoiceByUpi.getSenderUpiId (), receiverUpiId, invoiceByUpi.getSenderPin (), amount);

        if (transaction == null) {
            invoice.setPaymentStatus (InvoiceTransactionStatus.UNPAID);
            PaymentTransaction paymentTransaction = new PaymentTransaction ();
            paymentTransaction.setInvoice (invoice);
            paymentTransaction.setPaymentStatus (InvoiceTransactionStatus.FAILED);
            paymentTransaction.setPaymentAmount (amount);
            paymentTransaction.setPaymentType (PaymentType.UPI);
            paymentTransactionRepository.save (paymentTransaction);
        } else {
            invoice.setPaymentStatus (InvoiceTransactionStatus.PAID);
            PaymentTransaction paymentTransaction = new PaymentTransaction ();
            paymentTransaction.setInvoice (invoice);
            paymentTransaction.setPaymentStatus (InvoiceTransactionStatus.PAID);
            paymentTransaction.setPaymentAmount (amount);
            paymentTransaction.setPaymentType (PaymentType.UPI);
            paymentTransactionRepository.save (paymentTransaction);
        }
        invoice.setFinalAmount (amount);
        invoiceRepository.save (invoice);

        return transaction;
    }

    // Private method to validate InvoiceRequest
    private void validateInvoiceRequest (InvoiceRequest invoiceRequest) {
        if (invoiceRequest == null) {
            throw new BadRequestException ("Invoice request cannot be null");
        }
        if (invoiceRequest.getCustomerId () == null || invoiceRequest.getCustomerId ().isEmpty ()) {
            throw new BadRequestException ("Customer ID must be provided");
        }
        if (invoiceRequest.getUnits () == null || invoiceRequest.getUnits () <= 0) {
            throw new BadRequestException ("Units must be greater than 0");
        }
        if (invoiceRequest.getMonth () == null || invoiceRequest.getDueDate () == null) {
            throw new BadRequestException ("Month and due date must be provided");
        }
        if (invoiceRequest.getDueDate ().isBefore (LocalDate.now ())) {
            throw new BadRequestException ("Due date cannot be in the past");
        }
    }

    // Private method to find the applicable tariff based on connection type and units
    private Tariff findApplicableTariff (ConnectionType connectionType, Integer units) {
        return tariffRepository.findByConnectionType (connectionType).stream ()
                .filter (tariff -> tariff.getMinUnits () <= units && tariff.getMaxUnits () >= units)
                .findFirst ()
                .orElseThrow (() -> new ResourceNotFoundException ("No tariff found for the given units and connection type"));
    }

    // Private method to calculate the bill amount
    private Double calculateBillAmount (Integer units, Tariff applicableTariff) {
        return units * applicableTariff.getRatePerUnit ();
    }

    // Private method to map Invoice entity to InvoiceResponse DTO
    private InvoiceResponse mapToResponse (Invoice invoice, Tariff tariff, Customer customer) {
        return InvoiceResponse.builder ()
                .id (invoice.getId ())
                .month (invoice.getMonth ())
                .connectionId (invoice.getConnection ().getId ())
                .customerId (customer.getCustomerId ())
                .customerName (customer.getUser ().getFirstName () + " " + customer.getUser ().getLastName ())
                .customerEmail (customer.getUser ().getEmail ())
                .connectionType (invoice.getConnection ().getConnectionType ().getTypeName ())
                .dueDate (invoice.getDueDate ())
                .rateApplicable (tariff.getRatePerUnit ().toString ())
                .units (invoice.getUnits ())
                .billAmount (invoice.getBillAmount ())
                .finalAmount (invoice.getFinalAmount ())
                .paymentStatus (invoice.getPaymentStatus ())
                .createdAt (invoice.getCreatedAt ())
                .updatedAt (invoice.getUpdatedAt ())
                .build ();
    }

    // Private method to create the Invoice entity
    private Invoice getInvoice (InvoiceRequest invoiceRequest, Tariff applicableTariff, Connection connection) {
        Double billAmount = calculateBillAmount (invoiceRequest.getUnits (), applicableTariff);

        Invoice invoice = new Invoice ();
        invoice.setConnection (connection);
        invoice.setMonth (invoiceRequest.getMonth ().withDayOfMonth (1));
        invoice.setDueDate (invoiceRequest.getDueDate ());
        invoice.setUnits (invoiceRequest.getUnits ());
        invoice.setBillAmount (billAmount);
        invoice.setFinalAmount (billAmount);  // Final amount may include adjustments if needed
        invoice.setPaymentStatus (InvoiceTransactionStatus.PENDING);
        return invoice;
    }
}
