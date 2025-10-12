package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewCreateRequest {

    @NotNull
    private Integer hotelId;

    @NotNull
    private Integer reservationId;

    @NotNull
    @DecimalMin("1.0") @DecimalMax("5.0")
    private Double rating;

    @Size(max = 255)
    private String title;

    @Size(max = 2000)
    private String comment;
}