package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelListItemDto {
    private Integer id;
    private String  name;
    private String  city;
    private String  country;
    private Boolean petFriendly;
    private Double  ratingAvg;
    private Integer ratingCount;
    private Double  minPricePerNight;
}