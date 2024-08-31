CREATE TABLE IF NOT EXISTS flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(255) NOT NULL,
    departure_city VARCHAR(255) NOT NULL,
    arrival_city VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    seats_available INTEGER NOT NULL,
    price_per_seat DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS passengers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    passport_number VARCHAR(255) NOT NULL UNIQUE
);

-- Create Booking Table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    flight_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    booking_date TIMESTAMP NOT NULL,
    total_amount BIGINT NOT NULL,
    status VARCHAR NOT NULL,
    FOREIGN KEY (flight_id) REFERENCES flights(id),
    FOREIGN KEY (passenger_id) REFERENCES passengers(id)
);