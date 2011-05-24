package com.bloatit.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.UUID;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoicePdfGenerator {

    private final String filename;

    InvoicePdfGenerator(String sellerName,
                        String sellerAddress,
                        String sellerTaxIdentification,
                        Actor<?> recipientActor,
                        String contributorName,
                        String contributorAdress,
                        String deliveryName,
                        BigDecimal priceExcludingTax,
                        BigDecimal totalPrice,
                        String invoiceId) throws ExternalErrorException {

        createDirectory();
        filename = FrameworkConfiguration.getRessourcesDirStorage() + "/invoices/" + UUID.randomUUID().toString();

        Document document = new Document();
        // step 2

        try {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filename));
            } catch (FileNotFoundException e) {
                throw new ExternalErrorException("Failed to write a pdf in '" + filename + "'.", e);
            }

            // step 3
            document.open();
            // step 4
            document.add(new Paragraph("Ugly almost empty invoice !"));
        } catch (DocumentException e) {
            throw new ExternalErrorException("Failed to generate pdf.", e);
        }
        // step 5
        document.close();

    }

    String getPdfUrl() {
        return filename;
    }


    /**
     * Make sure there is a directory to store the files.
     */
    private static final void createDirectory() {

        String dir = FrameworkConfiguration.getRessourcesDirStorage() + "/invoices/";

        final File storeDir = new File(dir);
        if (!storeDir.exists()) {
            if (!storeDir.mkdirs()) {
                throw new BadProgrammerException("Couldn't create file " + dir);
            }
            Log.model().info("Created directory " + dir);
        }
    }

}
