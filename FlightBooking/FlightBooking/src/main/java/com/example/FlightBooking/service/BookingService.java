package com.example.FlightBooking.service;

import com.example.FlightBooking.model.Booking;
import com.example.FlightBooking.repository.BookingRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BookingService(BookingRepository bookingRepository, FlightService flightService, KafkaTemplate<String, String> kafkaTemplate) {
        this.bookingRepository = bookingRepository;
        this.flightService = flightService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Booking> createBooking(Booking booking) {
        return flightService.updateSeats(booking.getFlightId(), 1, true)
                .flatMap(flight -> {
                    booking.setStatus("confirmed");
                    return bookingRepository.save(booking)
                            .doOnSuccess(savedBooking -> kafkaTemplate.send("booking-topic", "Booking Created: " + savedBooking));
                });
    }

    public Mono<Booking> cancelBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .flatMap(booking -> {
                    return flightService.updateSeats(booking.getFlightId(), 1, false)
                            .flatMap(flight -> {
                                booking.setStatus("cancelled");
                                return bookingRepository.save(booking)
                                        .doOnSuccess(savedBooking -> kafkaTemplate.send("booking-topic", "Booking Cancelled: " + savedBooking));
                            });
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