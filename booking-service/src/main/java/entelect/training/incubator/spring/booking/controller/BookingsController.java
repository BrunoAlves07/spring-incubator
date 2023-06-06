package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.booking.Booking;
import entelect.training.incubator.spring.booking.model.booking.BookingSearchRequest;
import entelect.training.incubator.spring.booking.service.BookingsService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hsqldb.lib.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        log.info("Processing booking creation request for booking={}", booking);

        final Booking savedBooking = bookingsService.createBooking(booking);

        if (savedBooking != null) {
            log.trace("Booking created successfully");
            return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
        }

        log.warn("Booking could not be confirmed");
        return ResponseEntity.notFound().build();

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingsById(@PathVariable("id") Integer bookingId) {

        log.info("Processing booking search request for booking id={}", bookingId);

        Booking booking = bookingsService.getBookingById(bookingId);

        if (booking != null) {
            log.trace("Booking found");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        log.warn("No booking found for id: {}", bookingId);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBookings(@RequestBody BookingSearchRequest searchRequest) {
        log.info("Processing booking search request: {}", searchRequest);

        if (searchRequest == null ||
                (searchRequest.getCustomerId() == null && StringUtil.isEmpty(searchRequest.getReferenceNumber()))) {
            log.warn("Invalid search request received: {}", searchRequest);
            return ResponseEntity.badRequest().build();
        }

        List<Booking> bookings = bookingsService.searchBooking(searchRequest);

        if (bookings != null && !bookings.isEmpty()) {
            log.trace("Bookings found: {}", bookings);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }

        log.trace("No bookings found for request: {}", searchRequest);
        return ResponseEntity.notFound().build();
    }
}
