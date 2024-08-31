package com.example.FlightBooking.repository;

import com.example.FlightBooking.model.Passenger;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends ReactiveCrudRepository<Passenger, Long> {
}