package resource.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "mp3_files")
public class Mp3Resource {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String fileName;
    @Lob
    @NotNull
    private byte[] fileData;
    @NotNull
    private Instant uploadedAt = Instant.now();
}
