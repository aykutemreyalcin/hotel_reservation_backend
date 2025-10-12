package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationCreateRequest {

    @NotNull
    private Integer hotelId;

    @NotNull
    private Integer roomId;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    @Min(1)
    private Integer guests = 1;

    @Min(0)
    private Integer pets = 0;

    @Size(max = 1000)
    private String specialRequests;

    @Size(max = 10)
    private String currency; // boş gelirse room.currency kullanılabilir
}