package com.example.demo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationDto {
    private Integer id;
    private Integer hotelId;
    private Integer roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer guests;
    private Integer pets;
    private String status;     // pending | confirmed | cancelled | completed
    private Double totalPrice;
    private String currency;
}