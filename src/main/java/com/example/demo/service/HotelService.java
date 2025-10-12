package com.example.demo.service;

import com.example.demo.dto.request.HotelRegisterRequest;
import com.example.demo.dto.request.RoomCreateRequest;
import com.example.demo.dto.HotelDetailDto;
import com.example.demo.dto.HotelListItemDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.resource.entity.Hotel;
import com.example.demo.resource.entity.Room;
import com.example.demo.resource.repository.HotelRepository;
import com.example.demo.resource.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepo;
    private final RoomRepository roomRepo;

    // LIST
    @Transactional(readOnly = true)
    public List<HotelListItemDto> getHotels(String city, Boolean petFriendly) {
        var hotels = hotelRepo.search(city, petFriendly);
        return hotels.stream().map(h -> {
            var rooms = roomRepo.findByHotel(h);
            Double min = rooms.stream()
                    .map(Room::getPricePerNight)
                    .filter(p -> p != null)
                    .min(Comparator.naturalOrder())
                    .orElse(null);
            return new HotelListItemDto(
                    h.getId(), h.getName(), h.getCity(), h.getCountry(),
                    h.getPetFriendly(), h.getRatingAvg(), h.getRatingCount(), min
            );
        }).toList();
    }

    // DETAIL
    @Transactional(readOnly = true)
    public HotelDetailDto getHotelDetail(Integer hotelId) {
        var h = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("hotel not found"));

        var roomDtos = roomRepo.findByHotel(h).stream().map(r ->
                new RoomDto(
                        r.getId(),
                        r.getName(),
                        r.getType(),
                        r.getCapacity(),
                        r.getPricePerNight(),
                        r.getCurrency(),
                        r.getFreeCancellation()
                )
        ).toList();

        return new HotelDetailDto(
                h.getId(), h.getName(), h.getDescription(),
                h.getCity(), h.getCountry(), h.getAddress(),
                h.getLatitude(), h.getLongitude(),
                h.getPetFriendly(), h.getRatingAvg(), h.getRatingCount(),
                roomDtos
        );
    }

    // ADMIN: HOTEL REGISTER
    @Transactional
    public Integer registerHotel(HotelRegisterRequest req) {
        var h = Hotel.builder()
                .name(req.getName())
                .description(req.getDescription())
                .city(req.getCity())
                .country(req.getCountry())
                .address(req.getAddress())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .petFriendly(req.getPetFriendly())
                .ratingAvg(0.0)
                .ratingCount(0)
                .build();
        return hotelRepo.save(h).getId();
    }

    // ADMIN: ROOM CREATE
    @Transactional
    public Integer createRoom(Integer hotelId, RoomCreateRequest req) {
        var h = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("hotel not found"));
        var r = Room.builder()
                .hotel(h)
                .name(req.getName())
                .type(req.getType())
                .capacity(req.getCapacity())
                .pricePerNight(req.getPricePerNight())
                .currency(req.getCurrency())
                .freeCancellation(req.getFreeCancellation())
                .build();
        return roomRepo.save(r).getId();
    }
}