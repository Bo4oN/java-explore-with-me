package statsClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StatsClient {
    private final RestTemplate restTemplate;

    public StatsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Object> get() {
        return null;
    }
}
