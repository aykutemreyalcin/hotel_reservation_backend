package com.example.demo.resource.repository;

import com.example.demo.resource.entity.Hotel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("""
      select h from Hotel h
      where (:city is null or lower(h.city) = lower(:city))
        and (:pet is null or h.petFriendly = :pet)
    """)
    List<Hotel> search(@Param("city") String city, @Param("pet") Boolean petFriendly);
}