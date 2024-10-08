package com.example.FlightBooking.repository;

import com.example.FlightBooking.model.Flight;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {
}
