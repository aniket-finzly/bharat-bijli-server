package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.InvoiceItemDTO;
import com.finzly.bbc.dto.billing.mapper.InvoiceItemMapper;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.models.billing.InvoiceItem;
import com.finzly.bbc.models.billing.Tariff;
import com.finzly.bbc.repositories.billing.InvoiceItemRepository;
import com.finzly.bbc.repositories.billing.InvoiceRepository;
import com.finzly.bbc.repositories.billing.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for InvoiceItem entity
@Service
@Transactional
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceItemMapper invoiceItemMapper;
    private final InvoiceRepository invoiceRepository;
    private final TariffRepository tariffRepository;

    @Autowired
    public InvoiceItemService (InvoiceItemRepository invoiceItemRepository, InvoiceItemMapper invoiceItemMapper,
                               InvoiceRepository invoiceRepository, TariffRepository tariffRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemMapper = invoiceItemMapper;
        this.invoiceRepository = invoiceRepository;
        this.tariffRepository = tariffRepository;
    }

    // CRUD Operations
    public List<InvoiceItemDTO> getAllInvoiceItems () {
        return invoiceItemRepository.findAll ().stream ()
                .map (invoiceItemMapper::toInvoiceItemDTO)
                .collect (Collectors.toList ());
    }

    public Optional<InvoiceItemDTO> getInvoiceItemById (String id) {
        return invoiceItemRepository.findById (id).map (invoiceItemMapper::toInvoiceItemDTO);
    }

    public InvoiceItemDTO createInvoiceItem (InvoiceItemDTO invoiceItemDTO) {
        Invoice invoice = invoiceRepository.findById (invoiceItemDTO.getInvoiceId ()).orElseThrow ();
        Tariff tariff = tariffRepository.findById (invoiceItemDTO.getTariffId ()).orElseThrow ();
        InvoiceItem invoiceItem = invoiceItemMapper.toInvoiceItemEntity (invoiceItemDTO, invoice, tariff);
        return invoiceItemMapper.toInvoiceItemDTO (invoiceItemRepository.save (invoiceItem));
    }

    public InvoiceItemDTO updateInvoiceItem (String id, InvoiceItemDTO invoiceItemDTO) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById (id).orElseThrow ();
        invoiceItemMapper.updateInvoiceItemEntity (invoiceItem, invoiceItemDTO);
        return invoiceItemMapper.toInvoiceItemDTO (invoiceItemRepository.save (invoiceItem));
    }

    public void deleteInvoiceItem (String id) {
        invoiceItemRepository.deleteById (id);
    }

    // Searching and Filtering
    public List<InvoiceItemDTO> findInvoiceItemsByInvoiceId (String invoiceId) {
        return invoiceItemRepository.findByInvoiceId (invoiceId).stream ()
                .map (invoiceItemMapper::toInvoiceItemDTO)
                .collect (Collectors.toList ());
    }
}
