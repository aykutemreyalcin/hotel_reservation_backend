package com.example.demo.controller;

import com.example.demo.dto.request.HotelRegisterRequest;
import com.example.demo.dto.request.RoomCreateRequest;
import com.example.demo.dto.HotelDetailDto;
import com.example.demo.dto.HotelListItemDto;
import com.example.demo.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
@CrossOrigin
public class HotelController {

    private final HotelService service;

    @GetMapping
    public ResponseEntity<List<HotelListItemDto>> getHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean petFriendly
    ) {
        return ResponseEntity.ok(service.getHotels(city, petFriendly));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDetailDto> getHotelDetail(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(service.getHotelDetail(hotelId));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> registerHotel(
            @RequestBody HotelRegisterRequest request
    ) {
        Integer id = service.registerHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/{hotelId}/room")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> createRoom(
            @PathVariable Integer hotelId,
            @RequestBody RoomCreateRequest request
    ) {
        Integer id = service.createRoom(hotelId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}