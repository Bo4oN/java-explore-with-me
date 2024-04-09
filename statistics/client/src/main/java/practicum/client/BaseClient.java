package practicum.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import stats.StatsDto;
import stats.StatsDtoOut;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate restTemplate) {
        this.rest = restTemplate;
    }

    private static ResponseEntity<StatsDtoOut[]> prepareStatsResponse(ResponseEntity<StatsDtoOut[]> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected ResponseEntity<StatsDtoOut[]> get(String path, Map<String, Object> params) {
        return makeAndSendRequest(HttpMethod.GET, path, params, null);
    }

    protected ResponseEntity<StatsDtoOut[]> post(String path, StatsDto body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    private <T> ResponseEntity<StatsDtoOut[]> makeAndSendRequest(HttpMethod method,
                                                                 String path,
                                                                 Map<String, Object> params,
                                                                 @Nullable T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<StatsDtoOut[]> statsServerResponse;
        try {
            if (params != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, StatsDtoOut[].class, params);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, StatsDtoOut[].class);
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return prepareStatsResponse(statsServerResponse);
    }
}
