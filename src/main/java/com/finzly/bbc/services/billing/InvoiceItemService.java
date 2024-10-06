package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.InvoiceItemRequest;
import com.finzly.bbc.dtos.billing.InvoiceItemResponse;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.InvoiceItem;
import com.finzly.bbc.repositories.billing.InvoiceItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final ModelMapper modelMapper;

    public InvoiceItemResponse createInvoiceItem(InvoiceItemRequest request) {
        InvoiceItem invoiceItem = modelMapper.map(request, InvoiceItem.class);
        InvoiceItem savedInvoiceItem = invoiceItemRepository.save(invoiceItem);
        return modelMapper.map(savedInvoiceItem, InvoiceItemResponse.class);
    }

    public InvoiceItemResponse getInvoiceItemById(String id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice item not found with ID: " + id));
        return modelMapper.map(invoiceItem, InvoiceItemResponse.class);
    }

    public InvoiceItemResponse updateInvoiceItem(String id, InvoiceItemRequest request) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice item not found with ID: " + id));

        invoiceItem.setUnitsConsumed(request.getUnitsConsumed());
        invoiceItem.setChargeAmount(request.getChargeAmount());
        invoiceItem.setDescription(request.getDescription());

        InvoiceItem updatedInvoiceItem = invoiceItemRepository.save(invoiceItem);
        return modelMapper.map(updatedInvoiceItem, InvoiceItemResponse.class);
    }

    public void deleteInvoiceItem(String id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice item not found with ID: " + id));
        invoiceItemRepository.delete(invoiceItem);
    }
}
