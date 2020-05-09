package co.proarea.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data @NoArgsConstructor
@Table(name = "files")
public class DBFile {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String fileType;

    private String fileLink;

    @Lob
    @ToString.Exclude
    @JsonIgnore
    private byte[] data;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne//(fetch = FetchType.EAGER)
    private ProductUnit productUnit;

    public DBFile(String fileName, String fileType, byte[] data, ProductUnit productUnit) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.productUnit = productUnit;
    }
}
