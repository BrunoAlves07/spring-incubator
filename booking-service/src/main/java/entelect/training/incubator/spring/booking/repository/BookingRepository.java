package entelect.training.incubator.spring.booking.repository;

import entelect.training.incubator.spring.booking.model.booking.Booking;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {

    List<Booking> findByCustomerId(@Param("customerId") Integer customerId);

    List<Booking> findByReferenceNumber(@Param("refNumber") String referenceNumber);
}
