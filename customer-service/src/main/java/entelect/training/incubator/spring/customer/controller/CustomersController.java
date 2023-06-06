package entelect.training.incubator.spring.customer.controller;

import entelect.training.incubator.spring.customer.model.Customer;
import entelect.training.incubator.spring.customer.model.CustomerSearchRequest;
import entelect.training.incubator.spring.customer.service.CustomersService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("customers")
public class CustomersController {

    private final CustomersService customersService;

    public CustomersController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        log.info("Processing customer creation request for customer={}", customer);

        final Customer savedCustomer = customersService.createCustomer(customer);

        log.trace("Customer created");
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customersService.getCustomers();

        if (!customers.isEmpty()) {
            log.trace("Found customers");
            return new ResponseEntity<>(customers, HttpStatus.OK);
        }

        log.info("No customers could be found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        log.info("Processing customer search request for customer id={}", id);
        Customer customer = this.customersService.getCustomer(id);

        if (customer != null) {
            log.trace("Found customer");
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }

        log.trace("Customer not found");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchCustomers(@RequestBody CustomerSearchRequest searchRequest) {
        log.info("Processing customer search request for request {}", searchRequest);

        Customer customer = customersService.searchCustomers(searchRequest);

        if (customer != null) {
            return ResponseEntity.ok(customer);
        }

        log.trace("Customer not found");
        return ResponseEntity.notFound().build();
    }
}