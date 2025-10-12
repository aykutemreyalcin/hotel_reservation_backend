package com.example.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDto {
    private Integer id;
    private Integer hotelId;
    private Integer userId;
    private Double rating;
    private String title;
    private String comment;
    private String createdAt; // ISO-8601 string (Instant.toString)
}