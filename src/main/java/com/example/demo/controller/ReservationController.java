package com.example.demo.controller;

import com.example.demo.dto.request.ReservationCreateRequest;
import com.example.demo.dto.ReservationDto;
import com.example.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestAttribute("userId") Integer userId,
            @RequestBody ReservationCreateRequest request
    ) {
        return ResponseEntity.ok(service.createReservation(userId, request));
    }

    @GetMapping("/upcoming")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationDto>> getUpcomingReservations(
            @RequestAttribute("userId") Integer userId
    ) {
        return ResponseEntity.ok(service.getUpcomingReservations(userId));
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationDto>> getReservationHistory(
            @RequestAttribute("userId") Integer userId
    ) {
        return ResponseEntity.ok(service.getReservationHistory(userId));
    }
}