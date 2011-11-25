package com.bloatit.model.invoicePdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.model.Actor;
import com.bloatit.model.ModelConfiguration;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoicePdfGenerator {
    private static final int FACTURE_NUMBER_FONT_SIZE = 15;
    private static final int TAX_RATE_SCALE = 2;
    private static final int TAXES_TABLE_LEFT = 250;
    private static final int FOOTER_FONT_SIZE = 10;
    private static final float RIGHT_COLUMN = 250;
    private static final float PADDING_LEFT_HEADER_CELL = 5;
    private static final float PADDING_LEFT_BODY_CELL = 10;
    private static final float PADDING_RIGHT_BODY_CELL = 5;
    private static final float PADDING_BOTTOM_HEADER_CELL = 3;
    private static final float PADDING_TOP_BODY_CELL = 8;
    private static final float PADDING_BOTTOM_BODY_CELL = PADDING_TOP_BODY_CELL;
    private static final String KEYWORDS = "invoice, elveos.org, elveos";

    private final String filename;
    private final Rectangle pageSize;
    private final Document document;
    private PdfWriter writer;

    private static final float LEFT_MARGIN = 60;
    private static final float RIGHT_MARGIN = LEFT_MARGIN;
    private static final float TOP_MARGIN = 60;
    private static final float BOTTOM_MARGIN = 30;
    private final String invoiceType;
    private final String invoiceId;
    private final String sellerName;
    private final String sellerStreet;
    private final String sellerExtras;
    private final String sellerCity;
    private final String sellerCountry;
    private final String receiverName;
    private final String receiverStreet;
    private final String receiverExtras;
    private final String receiverCity;
    private final String receiverCountry;
    private final String receiverTaxIdentification;
    private final Date invoiceDate;
    private final String deliveryName;
    private final BigDecimal priceExcludingTax;
    private final BigDecimal taxRate;
    private final BigDecimal taxAmount;
    private final BigDecimal totalPrice;
    private final String sellerLegalId;
    private final String sellerTaxId;
    private final String logo;

    /**
     * Generates a pdf for an invoice
     * 
     * @param invoiceType the type of the invoice. This will be displayed in the
     *            title. Should be something like Elveos invoice, or Purchase on
     *            elveos website ...
     * @param invoiceId the unique id of the invoice
     * @param sellerName the name of the seller
     * @param sellerStreet the street of the seller (with house number)
     * @param sellerExtras extra information for the seller address. <b>can be
     *            null</b>
     * @param sellerCity the city of the seller, <i>including ZIP code</i>
     * @param sellerCountry the country of the seller
     * @param receiverName the name of the receiver
     * @param receiverStreet the street of the receiver
     * @param receiverExtras extra information of the receiver address. <b>can
     *            be null</b>
     * @param receiverCity the city of the receiver, <i>including ZIP code</i>
     * @param receiverCountry the country of the receiver
     * @param invoiceDate the creation date of the invoice
     * @param deliveryName the name of the item sold
     * @param priceExcludingTax the price of the item sold, without tax
     * @param taxRate the tax rate applied. <b>has to be <i>new
     *            BigDecimal(19.6)</i> for 19.6%</b>
     * @param taxAmount the amount of tax to pay
     * @param totalPrice the total price including taxes
     * @param sellerLegalId the seller legal identifier (legal registry number,
     *            plus company type ...)
     * @param sellerTaxId the seller tax identifier
     */
    public InvoicePdfGenerator(final String invoiceType,
                               final String invoiceId,
                               final String sellerName,
                               final String sellerStreet,
                               final String sellerExtras,
                               final String sellerCity,
                               final String sellerCountry,
                               final String receiverName,
                               final String receiverStreet,
                               final String receiverExtras,
                               final String receiverCity,
                               final String receiverCountry,
                               final String receiverTaxIdentification,
                               final Date invoiceDate,
                               final String deliveryName,
                               final BigDecimal priceExcludingTax,
                               final BigDecimal taxRate,
                               final BigDecimal taxAmount,
                               final BigDecimal totalPrice,
                               final String sellerLegalId,
                               final String sellerTaxId,
                               final String logo) {

        // We check that we didn't receive null or empty parameters
        if (invoiceType == null || invoiceId == null || sellerName == null || sellerStreet == null || sellerCity == null || sellerCountry == null
                || receiverName == null || receiverStreet == null || receiverCountry == null || invoiceDate == null || deliveryName == null
                || priceExcludingTax == null || taxRate == null || taxAmount == null || totalPrice == null || sellerLegalId == null) {
            throw new NonOptionalParameterException("No parameter except sellerExtras or receiverExtras can be null.");
        }
        if (invoiceType.isEmpty() || invoiceId.isEmpty() || sellerName.isEmpty() || sellerStreet.isEmpty() || sellerCity.isEmpty()
                || sellerCountry.isEmpty() || receiverName.isEmpty() || receiverStreet.isEmpty() || receiverCountry.isEmpty()
                || deliveryName.isEmpty() || sellerLegalId.isEmpty()) {
            throw new NonOptionalParameterException("No parameter can be empty.");
        }

        this.pageSize = PageSize.A4;
        this.document = new Document();
        this.invoiceType = invoiceType;

        String filename = FrameworkConfiguration.getRessourcesDirStorage() + "/invoices/" + invoiceId + ".pdf";
        int retry = 1;

        while (new File(filename).exists()) {
            filename = FrameworkConfiguration.getRessourcesDirStorage() + "/invoices/" + invoiceId + "-" + (retry++) + ".pdf";
        }

        this.filename = filename;
        this.invoiceId = invoiceId;
        this.sellerName = sellerName;
        this.sellerStreet = sellerStreet;
        this.sellerExtras = sellerExtras;
        this.sellerCity = sellerCity;
        this.sellerCountry = sellerCountry;
        this.receiverName = receiverName;
        this.receiverStreet = receiverStreet;
        this.receiverExtras = receiverExtras;
        this.receiverCity = receiverCity;
        this.receiverCountry = receiverCountry;
        this.receiverTaxIdentification = receiverTaxIdentification;
        this.invoiceDate = invoiceDate;
        this.deliveryName = deliveryName;
        this.priceExcludingTax = priceExcludingTax;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.totalPrice = totalPrice;
        this.sellerLegalId = sellerLegalId;
        this.sellerTaxId = sellerTaxId;
        this.logo = logo;
    }

    public String getPdfUrl() {

        createDirectory();

        try {
            write(new FileOutputStream(filename));
        } catch (final IOException e) {
            throw new ExternalErrorException("Failed load logo to generate invoice.", e);
        }

        return filename;
    }

    public void write(OutputStream output) {

        try {
            writer = PdfWriter.getInstance(document, output);
            document.open();
            addMetaData(invoiceType, invoiceId, sellerName);
            addLogoImg();
            addEmitter(sellerName, sellerStreet, sellerExtras, sellerCity, sellerCountry);
            addReceiver(receiverName, receiverStreet, receiverExtras, receiverCity, receiverCountry, receiverTaxIdentification);
            addDate(invoiceDate);
            addFactureNumber(invoiceId);
            addDetailTable(deliveryName, priceExcludingTax);
            if (taxAmount.compareTo(BigDecimal.ZERO) == 0) {
                addNoTaxesTable(totalPrice);
            } else {
                addTaxesTable(priceExcludingTax, taxRate, taxAmount, totalPrice);
            }
            addFooter(sellerName, sellerLegalId, sellerTaxId);
        } catch (final DocumentException e) {
            throw new ExternalErrorException("Failed to generate pdf.", e);
        } catch (final IOException e) {
            throw new ExternalErrorException("Failed load logo to generate invoice.", e);
        } catch (final InvalidPositionException e) {
            throw new BadProgrammerException("Added content out of the invoice bounds", e);
        }
        document.close();
    }

    /**
     * Adds metadata to the document
     * <p>
     * Example of use:
     * 
     * <pre>
     * addMetaData(&quot;Elveos Invoice&quot;, &quot;123&quot;, &quot;elveos.org&quot;, &quot;elveos.org&quot;);
     * </pre>
     * 
     * </p>
     * 
     * @param invoiceType the type of the invoice (elveos payment ...). This
     *            will be added to the title of the invoice
     * @param invoiceId the id of the invoice. Added to title, and into keywords
     * @param author The author of the invoice. Should elveos.org if its and
     *            elveos invoice
     */
    private void addMetaData(final String invoiceType, final String invoiceId, final String author) {
        document.addTitle(invoiceType + " " + invoiceId);
        document.addSubject(invoiceType);
        document.addAuthor(author);
        document.addCreator("elveos.org");
        document.addKeywords(KEYWORDS + ", " + author + ", " + invoiceId);
    }

    /**
     * Adds the linkeos image at the top of the invoice
     * 
     * @throws DocumentException when an error happens while writing the
     *             document
     * @throws InvalidPositionException if the image is placed at an incorrect
     *             position
     * @throws IOException if the image file cannot be read
     */
    private void addLogoImg() throws DocumentException, InvalidPositionException, IOException {
        if (logo == null) {
            return;
        }
        final Image img = Image.getInstance(logo);
        img.setAbsolutePosition(LEFT_MARGIN, pageSize.getHeight() - TOP_MARGIN - BOTTOM_MARGIN + 15);
        img.scalePercent(20);
        img.setCompressionLevel(8);
        document.add(img);
    }

    /**
     * Adds the emitter information to the invoice
     * 
     * @param sellerName Name of the seller
     * @param sellerStreet Street of the seller
     * @param sellerExtras Extra informations on seller's address. <b>Can be
     *            null</b>
     * @param sellerZIP ZIP code of the seller
     * @param sellerCountry Country of the seller
     * @throws DocumentException when there is an error while writing
     *             information on the document
     * @throws InvalidPositionException when some of the coordinates used are
     *             incorrect
     */
    private void addEmitter(final String sellerName,
                            final String sellerStreet,
                            final String sellerExtras,
                            final String sellerZIP,
                            final String sellerCountry) throws DocumentException, InvalidPositionException {
        final StringBuilder sb = new StringBuilder();
        sb.append(sellerName).append('\n');
        sb.append(sellerStreet).append('\n');
        if (sellerExtras != null) {
            sb.append(sellerExtras).append('\n');
        }
        sb.append(sellerZIP).append('\n');
        sb.append(sellerCountry).append('\n');
        final Paragraph p = new Paragraph(sb.toString());
        setLeft(730, p);
    }

    /**
     * Adds the receiver information to the invoice
     * 
     * @param receiverTaxIdentification
     * @param sellerName Name of the receiver
     * @param sellerStreet Street of the receiver
     * @param sellerExtras Extra informations on receiver's address. <b>Can be
     *            null</b>
     * @param sellerZIP ZIP code of the receiver
     * @param sellerCountry Country of the receiver
     * @throws DocumentException when there is an error while writing
     *             information on the document
     * @throws InvalidPositionException when some of the coordinates used are
     *             incorrect
     */
    private void addReceiver(final String receiverName,
                             final String receiverStreet,
                             final String receiverExtras,
                             final String receiverZIP,
                             final String receiverCountry,
                             final String receiverTaxIdentification) throws DocumentException, InvalidPositionException {
        final StringBuilder sb = new StringBuilder();
        sb.append(receiverName).append('\n');
        sb.append(receiverStreet).append('\n');
        if (receiverExtras != null) {
            sb.append(receiverExtras).append('\n');
        }
        sb.append(receiverZIP).append('\n');
        sb.append(receiverCountry).append('\n');
        if (receiverTaxIdentification != null) {
            sb.append("Tax id: ").append(receiverTaxIdentification).append('\n');
        }
        final Paragraph p = new Paragraph(sb.toString());

        setAt(RIGHT_COLUMN, 670, p);
    }

    /**
     * Adds the line for creation date
     * 
     * @param creationDate the date of creation
     * @throws DocumentException when an error happens when writing into the
     *             document
     * @throws InvalidPositionException when the programmer suck
     */
    private void addDate(final Date creationDate) throws DocumentException, InvalidPositionException {
        final Paragraph p = new Paragraph(new SimpleDateFormat("EEEE MMMM d, yyyy", Locale.US).format(creationDate) + ",");
        setAt(RIGHT_COLUMN, 550, p);
    }

    private void addFactureNumber(final String invoiceId) throws DocumentException, InvalidPositionException {
        final Paragraph p = new Paragraph("Invoice No: " + invoiceId);
        p.getFont().setSize(FACTURE_NUMBER_FONT_SIZE);
        p.getFont().setStyle(Font.BOLD);
        setLeft(480, p);
    }

    private void addDetailTable(final String deliveryName, final BigDecimal price) throws DocumentException, InvalidPositionException {
        final PdfPTable table = new PdfPTable(4);
        table.addCell(createTableHeaderCell("Description of service"));
        table.addCell(createTableHeaderCell("Qty"));
        table.addCell(createTableHeaderCell("Unit price"));
        table.addCell(createTableHeaderCell("Amount"));

        table.addCell(createTableBodyCell(deliveryName));
        table.addCell(createTableBodyCell("1"));
        table.addCell(createTableBodyCell(price));
        table.addCell(createTableBodyCell(price));

        table.setWidthPercentage(100);
        final float[] widths = { 300f, 66f, 120f, 85f };
        table.setWidths(widths);

        setLeft(430, table);
    }

    private void addNoTaxesTable(final BigDecimal totalPrice) throws DocumentException, InvalidPositionException {
        final PdfPTable table = new PdfPTable(2);
        table.addCell(createTableBodyCell("Total (no taxes included)"));
        table.addCell(createTableBodyCell(totalPrice));

        table.setWidthPercentage(100);
        final float[] widths = { 186f, 85f };
        table.setWidths(widths);

        setAt(TAXES_TABLE_LEFT, 375, table);
    }

    private void addTaxesTable(final BigDecimal amountNoTaxes,
                               final BigDecimal taxRate,
                               final BigDecimal taxAmount,
                               final BigDecimal amountTaxesIncluded) throws DocumentException, InvalidPositionException {
        final PdfPTable table = new PdfPTable(2);
        table.addCell(createTableBodyCell("Sub Total"));
        table.addCell(createTableBodyCell(amountNoTaxes));
        table.addCell(createTableBodyCell("Taxes ("
                + taxRate.multiply(new BigDecimal("100")).setScale(TAX_RATE_SCALE, BigDecimal.ROUND_HALF_EVEN).toPlainString() + " %)"));
        table.addCell(createTableBodyCell(taxAmount));
        table.addCell(createTableBodyCell("Total (taxes included)"));
        table.addCell(createTableBodyCell(amountTaxesIncluded));

        table.setWidthPercentage(100);
        final float[] widths = { 186f, 85f };
        table.setWidths(widths);

        setAt(TAXES_TABLE_LEFT, 375, table);
    }

    /**
     * Adds the footer of the invoice containing seller legal informations
     * 
     * @param sellerName the name of the seller
     * @param sellerId the legal identification of the seller, including company
     *            form, registry number ...
     * @param sellerTaxIdentification the tax id of the seller
     * @throws DocumentException when the document can't be written
     * @throws InvalidPositionException when the content is added in a place
     *             where it shouldn't
     */
    private void addFooter(final String sellerName, final String sellerId, final String sellerTaxIdentification)
            throws DocumentException, InvalidPositionException {
        final PdfContentByte cb = writer.getDirectContent();

        cb.setLineWidth(1.7f);
        cb.setGrayStroke(0.80f);
        cb.moveTo(0 + LEFT_MARGIN, 60 + BOTTOM_MARGIN);
        cb.lineTo(pageSize.getWidth() - RIGHT_MARGIN, 60 + BOTTOM_MARGIN);
        cb.stroke();

        final StringBuilder sb = new StringBuilder();
        sb.append(sellerName).append('\n');
        sb.append(sellerId).append('\n');
        if (sellerTaxIdentification != null) {
            sb.append("Tax id: ").append(sellerTaxIdentification).append('\n');
        }

        final Paragraph p = new Paragraph(sb.toString());
        p.setAlignment(Element.ALIGN_CENTER);
        p.getFont().setStyle(Font.ITALIC);
        p.getFont().setColor(70, 70, 70);
        p.getFont().setSize(FOOTER_FONT_SIZE);
        setAt(0, 60, p);
    }

    private void setLeft(final float y, final Element element) throws DocumentException, InvalidPositionException {
        setAt(0, y, element);
    }

    /**
     * Adds some content at a given position
     * <p>
     * 0.0 coordinates represents the bottom left corner of the page
     * </p>
     * 
     * @param x the distance to the the right of the bottom left corner
     * @param y the distance to the the top of the bottom left corner
     * @throws DocumentException
     * @throws InvalidPositionException
     */
    private void setAt(final float x, final float y, final Element element) throws DocumentException, InvalidPositionException {
        if ((pageSize.getWidth() - (LEFT_MARGIN + RIGHT_MARGIN)) < x || (pageSize.getHeight() - (TOP_MARGIN + BOTTOM_MARGIN)) < y) {
            throw new InvalidPositionException();
        }
        final ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(x + LEFT_MARGIN, y + BOTTOM_MARGIN, pageSize.getWidth() - RIGHT_MARGIN, 0);
        ct.addElement(element);
        ct.go();
    }

    /**
     * Create a table cell to use as a header
     * 
     * @param text the text to add into the cell
     * @return the cell
     */
    private static PdfPCell createTableHeaderCell(final String text) {
        final PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(new BaseColor(71, 162, 190));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingLeft(PADDING_LEFT_HEADER_CELL);
        cell.setPaddingBottom(PADDING_BOTTOM_HEADER_CELL);
        return cell;
    }

    /**
     * Create a table cell to use as a table body
     * 
     * @param text the text to add into the cell
     * @return the cell
     */
    private static PdfPCell createTableBodyCell(final String text) {
        final PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingBottom(PADDING_BOTTOM_BODY_CELL);
        cell.setPaddingTop(PADDING_TOP_BODY_CELL);
        cell.setPaddingLeft(PADDING_LEFT_BODY_CELL);
        return cell;
    }

    /**
     * Create a table cell to use as a header
     * 
     * @param text the euro amount to add into the cell
     * @return the cell
     */
    private static PdfPCell createTableBodyCell(final BigDecimal amount) {
        final PdfPCell cell = new PdfPCell(new Phrase(amount.toEngineeringString() + " €"));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingRight(PADDING_RIGHT_BODY_CELL);
        return cell;
    }

    /**
     * Make sure there is a directory to store the files.
     */
    private static final void createDirectory() {
        final String dir = FrameworkConfiguration.getRessourcesDirStorage() + "/invoices/";

        final File storeDir = new File(dir);
        if (!storeDir.exists()) {
            if (!storeDir.mkdirs()) {
                throw new BadProgrammerException("Couldn't create file " + dir);
            }
            Log.model().info("Created directory " + dir);
        }
    }

}
