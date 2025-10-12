package com.example.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomDto {
    private Integer id;
    private String name;
    private String type;            // single | double | suite
    private Integer capacity;
    private Double pricePerNight;
    private String currency;
    private Boolean freeCancellation;
}