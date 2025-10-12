package com.example.demo.service;

import com.example.demo.dto.request.ReviewCreateRequest;
import com.example.demo.dto.ReviewDto;
import com.example.demo.resource.entity.Hotel;
import com.example.demo.resource.entity.Reservation;
import com.example.demo.resource.entity.Review;
import com.example.demo.resource.entity.User;
import com.example.demo.resource.repository.HotelRepository;
import com.example.demo.resource.repository.ReservationRepository;
import com.example.demo.resource.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final HotelRepository hotelRepo;
    private final ReservationRepository reservationRepo;
    private final ReviewRepository reviewRepo;

    @Transactional
    public ReviewDto addReview(Integer userId, ReviewCreateRequest req) {
        Hotel hotel = hotelRepo.findById(req.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("hotel not found"));
        Reservation res = reservationRepo.findById(req.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("reservation not found"));

        if (!res.getUser().getId().equals(userId))
            throw new IllegalArgumentException("reservation not owned by user");
        if (!res.getHotel().getId().equals(hotel.getId()))
            throw new IllegalArgumentException("reservation not for this hotel");

        Review rv = Review.builder()
                .user(User.builder().id(userId).build())
                .hotel(hotel)
                .reservation(res)
                .rating(req.getRating())
                .title(req.getTitle())
                .comment(req.getComment())
                .build();

        rv = reviewRepo.save(rv);

        return new ReviewDto(
                rv.getId(), rv.getHotel().getId(), rv.getUser().getId(),
                rv.getRating(), rv.getTitle(), rv.getComment(),
                rv.getCreatedAt() == null ? null : rv.getCreatedAt().toString()
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getHotelReviews(Integer hotelId) {
        var hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("hotel not found"));
        return reviewRepo.findByHotelOrderByIdDesc(hotel).stream()
                .map(rv -> new ReviewDto(
                        rv.getId(), rv.getHotel().getId(), rv.getUser().getId(),
                        rv.getRating(), rv.getTitle(), rv.getComment(),
                        rv.getCreatedAt() == null ? null : rv.getCreatedAt().toString()
                )).toList();
    }
}