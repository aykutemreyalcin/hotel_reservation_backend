package com.example.demo.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomCreateRequest {
    private String name;
    private String type;         // single/double/suite
    private Integer capacity;
    private Double pricePerNight;
    private String currency;     // PLN
    private Boolean freeCancellation;
}