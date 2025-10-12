package com.example.demo.resource.repository;

import com.example.demo.resource.entity.Reservation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Belirli oda için tarih çakışması var mı (cancelled hariç)
    @Query("""
      select count(r)>0 from Reservation r
      where r.room.id = :roomId
        and r.status <> 'cancelled'
        and r.checkIn < :to and r.checkOut > :from
    """)
    boolean existsOverlap(@Param("roomId") Integer roomId,
                          @Param("from") LocalDate from,
                          @Param("to") LocalDate to);

    // Kullanıcı rezervasyonları
    @Query("""
      select r from Reservation r
      where r.user.id = :userId and r.checkIn >= :today and r.status in ('pending','confirmed')
      order by r.checkIn asc
    """)
    List<Reservation> findUpcoming(@Param("userId") Integer userId, @Param("today") LocalDate today);

    @Query("""
      select r from Reservation r
      where r.user.id = :userId and (r.checkOut < :today or r.status = 'completed')
      order by r.checkIn desc
    """)
    List<Reservation> findHistory(@Param("userId") Integer userId, @Param("today") LocalDate today);
}