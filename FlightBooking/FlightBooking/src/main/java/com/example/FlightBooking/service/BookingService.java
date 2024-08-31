package com.example.FlightBooking.service;

import com.example.FlightBooking.model.Booking;
import com.example.FlightBooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightService flightService;

    public BookingService(BookingRepository bookingRepository, FlightService flightService) {
        this.bookingRepository = bookingRepository;
        this.flightService = flightService;
    }

    public Mono<Booking> createBooking(Booking booking) {
        return flightService.updateSeats(booking.getFlightId(), 1) // Assume one seat per booking
                .then(bookingRepository.save(booking)
                        .flatMap(savedBooking -> {
                            savedBooking.setStatus("confirmed");
                            return bookingRepository.save(savedBooking);
                        }));
    }

    public Mono<Booking> cancelBooking(Long id) {
        return bookingRepository.findById(id)
                .flatMap(existingBooking -> {
                    existingBooking.setStatus("cancelled");
                    return flightService.updateSeats(existingBooking.getFlightId(), 1)
                            .then(bookingRepository.save(existingBooking));
                });
    }

    public Flux<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Mono<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Mono<Void> deleteBooking(Long id) {
        return bookingRepository.deleteById(id);
    }

    public Mono<Booking> updateBooking(Long id, Booking booking) {
        return bookingRepository.findById(id)
                .flatMap(existingBooking -> {
                    existingBooking.setFlightId(booking.getFlightId());
                    existingBooking.setPassengerId(booking.getPassengerId());
                    existingBooking.setBookingDate(booking.getBookingDate());
                    existingBooking.setStatus(booking.getStatus());
                    return bookingRepository.save(existingBooking);
                });
    }
}