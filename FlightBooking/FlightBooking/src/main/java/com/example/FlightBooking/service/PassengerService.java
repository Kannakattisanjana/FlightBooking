package com.example.FlightBooking.service;

import com.example.FlightBooking.model.Passenger;
import com.example.FlightBooking.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public Mono<Passenger> createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Mono<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    public Flux<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Mono<Passenger> updatePassenger(Long id, Passenger passenger) {
        return passengerRepository.findById(id)
                .flatMap(existingPassenger -> {
                    existingPassenger.setFirstName(passenger.getFirstName());
                    existingPassenger.setLastName(passenger.getLastName());
                    existingPassenger.setEmail(passenger.getEmail());
                    existingPassenger.setPassportNumber(passenger.getPassportNumber());
                    return passengerRepository.save(existingPassenger);
                });
    }

    public Mono<Void> deletePassenger(Long id) {
        return passengerRepository.deleteById(id);
    }
}