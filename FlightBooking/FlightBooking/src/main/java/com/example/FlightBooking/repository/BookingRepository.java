package com.example.FlightBooking.repository;

import com.example.FlightBooking.model.Booking;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends ReactiveCrudRepository<Booking, Long> {
}
