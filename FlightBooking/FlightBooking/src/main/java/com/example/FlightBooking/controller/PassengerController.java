package com.example.FlightBooking.controller;

import com.example.FlightBooking.model.Passenger;
import com.example.FlightBooking.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping
    public Mono<Passenger> createPassenger(@RequestBody Passenger passenger) {
        return passengerService.createPassenger(passenger);
    }

    @GetMapping("/{id}")
    public Mono<Passenger> getPassengerById(@PathVariable Long id) {
        return passengerService.getPassengerById(id);
    }

    @GetMapping
    public Flux<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @PutMapping("/{id}")
    public Mono<Passenger> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {
        return passengerService.updatePassenger(id, passenger);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePassenger(@PathVariable Long id) {
        return passengerService.deletePassenger(id);
    }
}