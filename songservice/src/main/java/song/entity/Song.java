package song.entity;

import com.drew.lang.annotations.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "song")
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @NotNull
    @Positive
    private Long id;

    @NotBlank(message = "Song name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Song artist is required")
    @Size(max = 100)
    private String artist;

    @NotBlank(message = "Song album is required")
    @Size(max = 100)
    private String album;

    @Pattern(regexp = "^([0-5]?[0-9]):([0-5]?[0-9])$", message = "Duration must be in mm:ss format with leading zeros")
    private String duration;

    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be between 1900 and 2099")
    private String year;
}
