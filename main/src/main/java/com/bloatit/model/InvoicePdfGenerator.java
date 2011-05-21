package com.bloatit.model;

import java.math.BigDecimal;

public class InvoicePdfGenerator {


    private FileMetadata pdfFileMetadata;


    InvoicePdfGenerator(String sellerName,
            String sellerAddress,
            String sellerTaxIdentification,
            Actor<?> recipientActor,
            String contributorName,
            String contributorAdress,
            String deliveryName,
            BigDecimal priceExcludingTax,
            BigDecimal totalPrice,
            String invoiceId) {

    }


    FileMetadata getPdf() {
        return pdfFileMetadata;
    }


}
