package com.example.demo.resource.repository;

import com.example.demo.resource.entity.Review;
import com.example.demo.resource.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByHotelOrderByIdDesc(Hotel hotel);
}