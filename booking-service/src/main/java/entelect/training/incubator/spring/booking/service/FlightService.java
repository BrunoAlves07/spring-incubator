package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.flight.Flight;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class FlightService {

    @Value("${flight-service.server.base-url}")
    private String serviceUrl;
    @Value("${flight-service.auth.username}")
    private String serviceUsername;
    @Value("${flight-service.auth.password}")
    private String servicePassword;
    private HttpHeaders headers;
    private RestTemplateBuilder restTemplate;

    @PostConstruct
    public void init() {
        this.headers = new HttpHeaders();
        this.headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        this.restTemplate = new RestTemplateBuilder().basicAuthentication(serviceUsername, servicePassword);
    }

    public Flight getFlightById(Integer id) {
        if (id != null) {
            String url = serviceUrl + "/" + id;
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            try {
                ResponseEntity<?> response = restTemplate.build().exchange(url, HttpMethod.GET, entity, Flight.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    return (Flight) response.getBody();
                }
            } catch (HttpClientErrorException e) {
                log.warn("Error encountered while retrieving flight details. Status: {}", e.getStatusCode());
                log.warn(e.getResponseBodyAsString());
            }
        }

        return null;
    }
}
