package com.example.FlightBooking;

import com.example.FlightBooking.model.Booking;
import com.example.FlightBooking.model.Flight;
import com.example.FlightBooking.repository.BookingRepository;
import com.example.FlightBooking.service.BookingService;
import com.example.FlightBooking.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@SpringBootTest
class FlightBookingApplicationTests {
	private BookingService bookingService;
	private FlightService flightService;
	private BookingRepository bookingRepository;
	private KafkaTemplate kafkaTemplate;

	@BeforeEach
	void setUp() {
		flightService = Mockito.mock(FlightService.class);
		bookingRepository = Mockito.mock(BookingRepository.class);
		kafkaTemplate = Mockito.mock(KafkaTemplate.class);
		bookingService = new BookingService(bookingRepository, flightService, kafkaTemplate);
	}

	@Test
	void testCreateBooking() {
		Flight flight = new Flight();
		flight.setId(1L);
		flight.setSeatsAvailable(10);

		Booking booking = new Booking();
		booking.setId(1L);
		booking.setFlightId(1L);
		booking.setPassengerId(1L);
		booking.setBookingDate(LocalDateTime.parse("2024-08-31T12:00:00"));
		booking.setStatus("pending");

		Mockito.when(flightService.updateSeats(1L, 1, true))
				.thenReturn(Mono.just(flight));
		Mockito.when(bookingRepository.save(booking))
				.thenReturn(Mono.just(booking));

		bookingService.createBooking(booking)
				.as(StepVerifier::create)
				.expectNextMatches(b -> b.getStatus().equals("confirmed"))
				.verifyComplete();
	}

	@Test
	void testCancelBooking() {
		Flight flight = new Flight();
		flight.setId(1L);
		flight.setSeatsAvailable(9);

		Booking booking = new Booking();
		booking.setId(1L);
		booking.setFlightId(1L);
		booking.setPassengerId(1L);
		booking.setBookingDate(LocalDateTime.parse("2024-08-31T12:00:00"));
		booking.setStatus("confirmed");

		Mockito.when(bookingRepository.findById(1L))
				.thenReturn(Mono.just(booking));
		Mockito.when(flightService.updateSeats(1L, 1, false))
				.thenReturn(Mono.just(flight));
		Mockito.when(bookingRepository.save(booking))
				.thenReturn(Mono.just(booking));

		bookingService.cancelBooking(1L)
				.as(StepVerifier::create)
				.expectNextMatches(b -> b.getStatus().equals("cancelled"))
				.verifyComplete();
	}

	@Test
	void testCreateBookingNotEnoughSeats() {
		Flight flight = new Flight();
		flight.setId(1L);
		flight.setSeatsAvailable(0);

		Booking booking = new Booking();
		booking.setId(1L);
		booking.setFlightId(1L);
		booking.setPassengerId(1L);
		booking.setBookingDate(LocalDateTime.parse("2024-08-31T12:00:00"));
		booking.setStatus("pending");

		Mockito.when(flightService.updateSeats(1L, 1, true))
				.thenReturn(Mono.error(new RuntimeException("Not enough seats available")));

		bookingService.createBooking(booking)
				.as(StepVerifier::create)
				.expectErrorMatches(e -> e.getMessage().equals("Not enough seats available"))
				.verify();
	}
}
