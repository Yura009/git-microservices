package resource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "mp3_files")
public class Mp3Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_data", nullable = false, columnDefinition = "bytea")
    private byte[] fileData;
}
