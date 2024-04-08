package practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import stats.StatsDto;
import stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public StatsClient(@Value("${stat-server.url}") String url, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void saveStats(StatsDto statsDto) {
        log.info("Сохранение статистики - {}", statsDto);
        //return Arrays.asList(Objects.requireNonNull(
        post("/hit", statsDto);
    }

    public List<StatsDtoOut> getStats(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris,
                                      Boolean isUnique) {
        Map<String, Object> params = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", String.join(",", uris),
                "unique", isUnique
        );
        ResponseEntity<StatsDtoOut[]> response = get(
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                params);
        if (response.getBody() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
