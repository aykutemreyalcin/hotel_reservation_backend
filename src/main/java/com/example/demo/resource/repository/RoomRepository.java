package com.example.demo.resource.repository;

import com.example.demo.resource.entity.Room;
import com.example.demo.resource.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByHotel(Hotel hotel);
}