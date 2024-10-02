package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.InvoiceDTO;
import com.finzly.bbc.dto.billing.mapper.InvoiceMapper;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.repositories.billing.ConnectionRepository;
import com.finzly.bbc.repositories.billing.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for Invoice entity
@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final ConnectionRepository connectionRepository;

    @Autowired
    public InvoiceService (InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, ConnectionRepository connectionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.connectionRepository = connectionRepository;
    }

    // CRUD Operations
    public List<InvoiceDTO> getAllInvoices () {
        return invoiceRepository.findAll ().stream ()
                .map (invoiceMapper::toInvoiceDTO)
                .collect (Collectors.toList ());
    }

    public Optional<InvoiceDTO> getInvoiceById (String id) {
        return invoiceRepository.findById (id).map (invoiceMapper::toInvoiceDTO);
    }

    public InvoiceDTO createInvoice (InvoiceDTO invoiceDTO) {
        Connection connection = connectionRepository.findById (invoiceDTO.getConnectionId ()).orElseThrow ();
        Invoice invoice = invoiceMapper.toInvoiceEntity (invoiceDTO, connection);
        return invoiceMapper.toInvoiceDTO (invoiceRepository.save (invoice));
    }

    public InvoiceDTO updateInvoice (String id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById (id).orElseThrow ();
        invoiceMapper.updateInvoiceEntity (invoice, invoiceDTO);
        return invoiceMapper.toInvoiceDTO (invoiceRepository.save (invoice));
    }

    public void deleteInvoice (String id) {
        invoiceRepository.deleteById (id);
    }

    // Searching and Filtering
    public List<InvoiceDTO> getInvoicesByConnectionId (String connectionId) {
        return invoiceRepository.findByConnectionId (connectionId).stream ()
                .map (invoiceMapper::toInvoiceDTO)
                .collect (Collectors.toList ());
    }

    public List<InvoiceDTO> getOverdueInvoices () {
        return invoiceRepository.findByDueDateBefore (LocalDateTime.now ()).stream ()
                .map (invoiceMapper::toInvoiceDTO)
                .collect (Collectors.toList ());
    }
}
