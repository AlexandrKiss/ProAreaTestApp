package co.proarea.pdf;

import co.proarea.models.ProductUnit;
import co.proarea.models.InvoiceRow;
import co.proarea.services.ProductService;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Invoice {
    private final ProductService productService;
    private final List<InvoiceRow> invoiceRows;

    public Invoice(ProductService productService, List<InvoiceRow> invoiceRows) {
        this.productService = productService;
        this.invoiceRows = invoiceRows;
    }

    public ResponseEntity<byte[]> toResponseEntity() throws IOException {
        byte[] pdf = newInvoice();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    private byte[] newInvoice() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(getImage(document));
        document.add(getFrom(document));
        document.add(getOrder(document));

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private Table getFrom(Document document){
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd'T'hh:mm:ss");
        int fontSize = 10;
        int countColumn = 0;

        float [] pointColumnWidths = {100F, 100F};
        Table table = new Table(pointColumnWidths);

        List<String> cellVal = List.of(
                "Order Date:",formatForDateNow.format(dateNow),
                "Order Number:","1",
                "Restaurant Name:","Atwater",
                "Address:","1500 Av Atwater",
                "City:","Montreal");
        for(String s: cellVal) {
            Cell cell = new Cell();
            cell.add(s)
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(fontSize)
                    .setHeight((float) (fontSize*1.5));
            countColumn++;
            table.addCell(cell);
        }
        float y = (float) (PageSize.A4.getHeight() -
                document.getTopMargin() -
                countColumn * fontSize * 1.5 -
                75);
        table.setFixedPosition(document.getLeftMargin(),y, 200);
        return table;
    }

    private Image getImage (Document document) throws MalformedURLException {
        String imFile = "src/main/resources/McDonalds.png";
        ImageData data = ImageDataFactory.create(imFile);
        Image image = new Image(data);
        image.scaleAbsolute(75,75);
        float x = (PageSize.A4.getWidth() - image.getImageScaledWidth()) / 2;
        float y = (PageSize.A4.getHeight() - image.getImageScaledHeight() - document.getTopMargin());
        image.setFixedPosition(x, y);
        return image;
    }
    private Table getOrder(Document document) throws IOException {
        int fontSize = 10;
        int countColumn = 0;
        float total=0;

        float [] pointColumnWidths = {25F, 25F, 200F, 50F, 50F, 75F};
        Table table = new Table(pointColumnWidths);

        for(int i = -1; i < invoiceRows.size(); i++) {
            Cell num = new Cell();
            Cell ean = new Cell();
            Cell name = new Cell();
            Cell quantity = new Cell();
            Cell price = new Cell();
            Cell sum = new Cell();
            Cell[] cells = {num, ean, name, quantity, price, sum};

            if(i == -1) {
                String[] colNames = {"#","EAN", "Name", "Quantity", "Unit Price", "Amount"};
                for (int a = 0; a < cells.length; a++) {
                    cells[a].add(colNames[a])
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(Color.LIGHT_GRAY)
                            .setFontSize(fontSize)
                            .setHeight((float) (fontSize * 1.5));
                }
                table.addCell(num).addCell(ean).addCell(name).addCell(quantity).addCell(price).addCell(sum);
                countColumn++;
                continue;
            } else {
                for (Cell cell : cells) {
                    cell.setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(fontSize)
                            .setHeight((float) (fontSize * 1.5));
                }
            }
            ProductUnit productUnit = productService.getProductUnit(invoiceRows.get(i).getEan());
            if (productUnit != null) {
                num.add(""+countColumn);
                ean.add(""+productUnit.getEan());
                name.add(productUnit.getName()).setTextAlignment(TextAlignment.LEFT);
                quantity.add(""+invoiceRows.get(i).getCount());
                price.add(String.format("%s%.2f", "$", productUnit.getPrice()))
                        .setTextAlignment(TextAlignment.RIGHT);
                sum.add(String.format("%s%.2f", "$", invoiceRows.get(i).getCount() * productUnit.getPrice()))
                        .setTextAlignment(TextAlignment.RIGHT);

                table.addCell(num).addCell(ean).addCell(name).addCell(quantity).addCell(price).addCell(sum);
                total+=invoiceRows.get(i).getCount() * productUnit.getPrice();
                countColumn++;
            }
        }
        PdfFont titleFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        table.addCell(new Cell(0, countColumn)
                .add(String.format("%s%.2f","Total: $",total))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(titleFont)
                .setFontSize(fontSize)
                .setHeight((float) (fontSize*1.5)));
        countColumn++;

        float y = (float) (PageSize.A4.getHeight() -
                document.getTopMargin() - 275 -
                countColumn * fontSize * 1.5);
        table.setFixedPosition(document.getLeftMargin(),y,425);
        table.useAllAvailableWidth();
        return table;
    }
}