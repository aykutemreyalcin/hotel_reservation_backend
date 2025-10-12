package com.example.demo.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HotelRegisterRequest {
    private String name;
    private String description;
    private String city;
    private String country;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean petFriendly;
}