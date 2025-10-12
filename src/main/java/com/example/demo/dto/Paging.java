package com.example.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paging {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
}