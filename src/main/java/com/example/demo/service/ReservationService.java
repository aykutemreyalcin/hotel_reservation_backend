package com.example.demo.service;

import com.example.demo.dto.request.ReservationCreateRequest;
import com.example.demo.dto.ReservationDto;
import com.example.demo.resource.entity.Hotel;
import com.example.demo.resource.entity.Reservation;
import com.example.demo.resource.entity.Room;
import com.example.demo.resource.entity.User;
import com.example.demo.resource.repository.HotelRepository;
import com.example.demo.resource.repository.ReservationRepository;
import com.example.demo.resource.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final HotelRepository hotelRepo;
    private final RoomRepository roomRepo;
    private final ReservationRepository reservationRepo;

    @Transactional
    public ReservationDto createReservation(Integer userId, ReservationCreateRequest req) {
        Hotel hotel = hotelRepo.findById(req.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("hotel not found"));
        Room room = roomRepo.findById(req.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("room not found"));

        if (!room.getHotel().getId().equals(hotel.getId()))
            throw new IllegalArgumentException("room does not belong to hotel");

        if (req.getCheckIn() == null || req.getCheckOut() == null
                || !req.getCheckIn().isBefore(req.getCheckOut()))
            throw new IllegalArgumentException("invalid date range");

        boolean overlap = reservationRepo.existsOverlap(
                room.getId(), req.getCheckIn(), req.getCheckOut());
        if (overlap) throw new IllegalArgumentException("room not available");

        long nights = ChronoUnit.DAYS.between(req.getCheckIn(), req.getCheckOut());
        if (room.getPricePerNight() == null)
            throw new IllegalArgumentException("room price missing");

        double total = room.getPricePerNight() * nights;

        Reservation res = Reservation.builder()
                .user(User.builder().id(userId).build())
                .hotel(hotel)
                .room(room)
                .checkIn(req.getCheckIn())
                .checkOut(req.getCheckOut())
                .guests(req.getGuests())
                .pets(req.getPets())
                .status("confirmed")
                .totalPrice(total)
                .currency(req.getCurrency() != null ? req.getCurrency() : room.getCurrency())
                .specialRequests(req.getSpecialRequests())
                .build();

        res = reservationRepo.save(res);

        return new ReservationDto(
                res.getId(), res.getHotel().getId(), res.getRoom().getId(),
                res.getCheckIn(), res.getCheckOut(),
                res.getGuests(), res.getPets(),
                res.getStatus(), res.getTotalPrice(), res.getCurrency()
        );
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getUpcomingReservations(Integer userId) {
        var list = reservationRepo.findUpcoming(userId, LocalDate.now());
        return list.stream().map(r -> new ReservationDto(
                r.getId(), r.getHotel().getId(), r.getRoom().getId(),
                r.getCheckIn(), r.getCheckOut(),
                r.getGuests(), r.getPets(),
                r.getStatus(), r.getTotalPrice(), r.getCurrency()
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getReservationHistory(Integer userId) {
        var list = reservationRepo.findHistory(userId, LocalDate.now());
        return list.stream().map(r -> new ReservationDto(
                r.getId(), r.getHotel().getId(), r.getRoom().getId(),
                r.getCheckIn(), r.getCheckOut(),
                r.getGuests(), r.getPets(),
                r.getStatus(), r.getTotalPrice(), r.getCurrency()
        )).toList();
    }
}