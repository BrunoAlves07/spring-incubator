package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.booking.Booking;
import entelect.training.incubator.spring.booking.model.booking.BookingSearchRequest;
import entelect.training.incubator.spring.booking.model.customer.Customer;
import entelect.training.incubator.spring.booking.model.flight.Flight;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookingsService {

    private final BookingRepository bookingRepository;

    private final CustomerService customerService;
    private final FlightService flightService;

    @Autowired
    public BookingsService(
            BookingRepository bookingRepository,
            CustomerService customerService,
            FlightService flightService
    ) {
        this.bookingRepository = bookingRepository;
        this.customerService = customerService;
        this.flightService = flightService;
    }

    public Booking createBooking(Booking booking) {

        boolean isValidCustomer = isValidCustomer(booking.getCustomerId());

        if (!isValidCustomer) {
            log.warn("No existing customer found for id: {}", booking.getCustomerId());
            return null;
        }

        log.info("Existing customer found. Checking flight details...");

        boolean isValidFlight = isValidFlight(booking.getFlightId());

        if (!isValidFlight) {
            log.warn("Flight id: {} does not exist", booking.getFlightId());
            return null;
        }

        log.info("Flight details found. Confirming booking for details: {}", booking);
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> searchBooking(BookingSearchRequest searchRequest) {
        List<Booking> bookings = new ArrayList<>();

        if (searchRequest == null ||
                (searchRequest.getCustomerId() == null && StringUtil.isEmpty(searchRequest.getReferenceNumber()))) {
            return bookings;
        }

        Iterable<Booking> bookingIterable;

        if (!StringUtil.isEmpty(searchRequest.getReferenceNumber())) {
            bookingIterable = bookingRepository.findByReferenceNumber(searchRequest.getReferenceNumber());
        } else {
            bookingIterable = bookingRepository.findByCustomerId(searchRequest.getCustomerId());
        }

        List<Booking> result = new ArrayList<>();
        bookingIterable.forEach(result::add);
        return result;
    }

    private boolean isValidCustomer(Integer customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return customer != null;
    }

    private boolean isValidFlight(Integer flightId) {
        Flight flight = flightService.getFlightById(flightId);
        return flight != null;
    }
}
