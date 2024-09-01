package com.example.FlightBooking.service;

import com.example.FlightBooking.model.Flight;
import com.example.FlightBooking.repository.FlightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flux<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Mono<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Mono<Flight> createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Mono<Flight> updateFlight(Long id, Flight flight) {
        return flightRepository.findById(id)
                .flatMap(existingFlight -> {
                    existingFlight.setFlightNumber(flight.getFlightNumber());
                    existingFlight.setDepartureCity(flight.getDepartureCity());
                    existingFlight.setArrivalCity(flight.getArrivalCity());
                    existingFlight.setDepartureTime(flight.getDepartureTime());
                    existingFlight.setSeatsAvailable(flight.getSeatsAvailable());
                    existingFlight.setPricePerSeat(flight.getPricePerSeat());
                    return flightRepository.save(existingFlight);
                });
    }

    public Mono<Void> deleteFlight(Long id) {
        return flightRepository.deleteById(id);
    }

    public Mono<Flight> updateSeats(Long flightId, Integer seats, boolean isBooking) {
        return flightRepository.findById(flightId)
                .flatMap(flight -> {
                    int newSeatsAvailable = isBooking
                            ? flight.getSeatsAvailable() - seats
                            : flight.getSeatsAvailable() + seats;

                    if (newSeatsAvailable < 0) {
                        return Mono.error(new RuntimeException("Not enough seats available"));
                    }

                    flight.setSeatsAvailable(newSeatsAvailable);
                    return flightRepository.save(flight);
                });
    }
}