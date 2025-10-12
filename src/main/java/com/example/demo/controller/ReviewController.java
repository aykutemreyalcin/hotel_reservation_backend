package com.example.demo.controller;

import com.example.demo.dto.request.ReviewCreateRequest;
import com.example.demo.dto.ReviewDto;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@CrossOrigin
public class ReviewController {

    private final ReviewService service;

    @GetMapping("/{hotelId}")
    public ResponseEntity<List<ReviewDto>> getHotelReviews(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(service.getHotelReviews(hotelId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDto> addReview(
            @RequestAttribute("userId") Integer userId,
            @RequestBody ReviewCreateRequest request
    ) {
        ReviewDto review = service.addReview(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
}