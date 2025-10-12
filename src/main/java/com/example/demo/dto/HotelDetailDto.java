package com.example.demo.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDetailDto {
    private Integer id;
    private String  name;
    private String  description;
    private String  city;
    private String  country;
    private String  address;
    private Double  latitude;
    private Double  longitude;
    private Boolean petFriendly;
    private Double  ratingAvg;
    private Integer ratingCount;
    private List<RoomDto> rooms;
}