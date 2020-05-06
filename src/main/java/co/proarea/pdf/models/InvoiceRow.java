package co.proarea.pdf.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @ToString
public class InvoiceRow {
    @Getter
    private long ean;
    @Getter
    private int count;
}
