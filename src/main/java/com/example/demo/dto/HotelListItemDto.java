package com.example.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HotelListItemDto {
    private Integer id;
    private String name;
    private String city;
    private Boolean petFriendly;
    private Double ratingAvg;
    private Integer ratingCount;
    private Double minPricePerNight; // rooms MIN()

    public HotelListItemDto(Integer id, String name, String city, String country, Boolean petFriendly, Double ratingAvg, Integer ratingCount, Double min) {
    }
}