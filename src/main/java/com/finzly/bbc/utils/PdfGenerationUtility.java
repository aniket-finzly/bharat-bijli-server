package com.finzly.bbc.utils;

import com.finzly.bbc.dtos.billing.InvoiceResponse;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class PdfGenerationUtility {

    public byte[] generateInvoicePdf (InvoiceResponse invoice) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        Document document = new Document (PageSize.A4);
        PdfWriter.getInstance (document, baos);

        document.open ();

        // Add header with logo and company details
        addHeader (document);

        // Add invoice title
        Font titleFont = FontFactory.getFont (FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
        Paragraph title = new Paragraph ("ELECTRICITY BILL", titleFont);
        title.setAlignment (Element.ALIGN_CENTER);
        document.add (title);
        document.add (Chunk.NEWLINE);

        // Customer Details Section
        PdfPTable customerTable = new PdfPTable (2);
        customerTable.setWidthPercentage (100);
        customerTable.setSpacingBefore (10f);
        customerTable.setSpacingAfter (10f);

        addTableRow (customerTable, "Invoice ID:", invoice.getId ());
        addTableRow (customerTable, "Connection ID:", invoice.getConnectionId ());
        addTableRow (customerTable, "Customer ID:", invoice.getCustomerId ());
        addTableRow (customerTable, "Customer Name:", invoice.getCustomerName ());
        addTableRow (customerTable, "Customer Email:", invoice.getCustomerEmail ());
        addTableRow (customerTable, "Connection Type:", invoice.getConnectionType ());

        document.add (customerTable);

        // Bill Details Section
        PdfPTable billTable = new PdfPTable (2);
        billTable.setWidthPercentage (100);
        billTable.setSpacingBefore (10f);
        billTable.setSpacingAfter (10f);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern ("MMMM yyyy");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern ("dd MMM yyyy");

        addTableRow (billTable, "Billing Month:", invoice.getMonth ().format (monthFormatter));
        addTableRow (billTable, "Due Date:", invoice.getDueDate ().format (dateFormatter));
        addTableRow (billTable, "Rate Applicable:", invoice.getRateApplicable ());
        addTableRow (billTable, "Units Consumed:", invoice.getUnits ().toString ());
        addTableRow (billTable, "Bill Amount:", String.format ("₹%.2f", invoice.getBillAmount ()));
        addTableRow (billTable, "Final Amount:", String.format ("₹%.2f", invoice.getFinalAmount ()));
        addTableRow (billTable, "Payment Status:", invoice.getPaymentStatus ().toString ());

        document.add (billTable);

        // Additional Details Section
        if (invoice.getCreatedAt () != null || invoice.getUpdatedAt () != null) {
            PdfPTable timestampTable = new PdfPTable (2);
            timestampTable.setWidthPercentage (100);
            timestampTable.setSpacingBefore (10f);

            DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern ("dd MMM yyyy HH:mm:ss");

            if (invoice.getCreatedAt () != null) {
                addTableRow (timestampTable, "Created At:", invoice.getCreatedAt ().format (timestampFormatter));
            }
            if (invoice.getUpdatedAt () != null) {
                addTableRow (timestampTable, "Updated At:", invoice.getUpdatedAt ().format (timestampFormatter));
            }

            document.add (timestampTable);
        }

        // Add payment instructions (optional)
        addPaymentInstructions (document);

        // Add footer
        addFooter (document);

        document.close ();
        return baos.toByteArray ();
    }

    private void addHeader (Document document) throws Exception {
        PdfPTable headerTable = new PdfPTable (2);
        headerTable.setWidthPercentage (100);
        headerTable.setWidths (new float[]{1, 2});

        // Add company logo
        Image logo = Image.getInstance (Objects.requireNonNull (getClass ().getClassLoader ().getResource ("static/logo.png")).toString ());
        logo.scaleToFit (50, 50);
        PdfPCell logoCell = new PdfPCell (logo);
        logoCell.setBorder (Rectangle.NO_BORDER);
        headerTable.addCell (logoCell);

        // Add company information with styling
        Paragraph companyDetails = new Paragraph ();
        companyDetails.add (new Chunk ("Finzly Billing & Banking Corp.\n",
                FontFactory.getFont (FontFactory.HELVETICA_BOLD, 14, Color.DARK_GRAY)));
        companyDetails.add (new Chunk ("123 Fintech Street, Pune, Maharashtra 411014\n",
                FontFactory.getFont (FontFactory.HELVETICA, 10, Color.DARK_GRAY)));
        companyDetails.add (new Chunk ("Phone: +91 20 6763 6763",
                FontFactory.getFont (FontFactory.HELVETICA, 10, Color.DARK_GRAY)));

        PdfPCell detailsCell = new PdfPCell (companyDetails);
        detailsCell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        detailsCell.setBorder (Rectangle.NO_BORDER);
        headerTable.addCell (detailsCell);

        document.add (headerTable);
    }

    private void addFooter (Document document) throws DocumentException {
        document.add (Chunk.NEWLINE);

        Paragraph footer = new Paragraph ();
        footer.add (new Chunk ("Thank you for your business!\n",
                FontFactory.getFont (FontFactory.HELVETICA_BOLD, 12, Color.BLUE)));
        footer.add (new Chunk ("For any queries, please contact our customer support.",
                FontFactory.getFont (FontFactory.HELVETICA, 10, Color.DARK_GRAY)));
        footer.setAlignment (Element.ALIGN_CENTER);

        document.add (footer);
    }

    private void addPaymentInstructions (Document document) throws DocumentException {
        document.add (Chunk.NEWLINE);

        Paragraph paymentInstructions = new Paragraph ();
        paymentInstructions.add (new Chunk ("Payment Instructions:\n",
                FontFactory.getFont (FontFactory.HELVETICA_BOLD, 12, Color.BLACK)));
        paymentInstructions.add (new Chunk ("Please visit our website for payment details.",
                FontFactory.getFont (FontFactory.HELVETICA, 10, Color.DARK_GRAY)));

        paymentInstructions.setAlignment (Element.ALIGN_CENTER);
        document.add (paymentInstructions);
    }

    private void addTableRow (PdfPTable table, String label, String value) {
        Font labelFont = FontFactory.getFont (FontFactory.HELVETICA_BOLD, 10, Color.DARK_GRAY);
        Font valueFont = FontFactory.getFont (FontFactory.HELVETICA, 10, Color.BLACK);

        PdfPCell labelCell = new PdfPCell (new Phrase (label, labelFont));
        labelCell.setBorder (Rectangle.NO_BORDER);
        labelCell.setPadding (5f);
        labelCell.setBackgroundColor (Color.LIGHT_GRAY); // Adds alternate row color
        table.addCell (labelCell);

        PdfPCell valueCell = new PdfPCell (new Phrase (value, valueFont));
        valueCell.setBorder (Rectangle.NO_BORDER);
        valueCell.setPadding (5f);
        valueCell.setHorizontalAlignment (Element.ALIGN_RIGHT); // Align values to the right
        table.addCell (valueCell);
    }
}
