package co.proarea.pdf.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor @ToString
@NoArgsConstructor
public class InvoiceRow {
    @Getter
    private long ean;
    @Getter
    private int count;
}
