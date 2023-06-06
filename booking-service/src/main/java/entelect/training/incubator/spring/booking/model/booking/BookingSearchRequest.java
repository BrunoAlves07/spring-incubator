package entelect.training.incubator.spring.booking.model.booking;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingSearchRequest {

    private Integer customerId;

    private String referenceNumber;
}
