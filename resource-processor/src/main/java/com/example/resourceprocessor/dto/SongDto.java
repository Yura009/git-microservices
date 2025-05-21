package com.example.resourceprocessor.dto;

import com.drew.lang.annotations.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SongDto {
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String artist;

    @NotBlank
    @Size(max = 100)
    private String album;

    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Duration must be in mm:ss format")
    private String duration;

    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be between 1900 and 2099")
    private String year;
}
