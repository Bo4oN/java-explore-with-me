package practicum.service.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.client.StatsClient;
import practicum.model.Event;
import practicum.repository.RequestRepository;
import stats.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatServiceImpl implements StatService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RequestRepository requestRepository;

    private final StatsClient statClient;

    private final ObjectMapper objectMapper;

    @Value("${main_app}")
    private String app;

    @Override
    public Map<Long, Long> toConfirmedRequest(Collection<Event> list) {
        return null;
    }

    @Override
    public Map<Long, Long> toView(Collection<Event> list) {
        return null;
    }

    @Transactional
    @Override
    public void addHits(HttpServletRequest request) {
        statClient.saveStats(new StatsDto(app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));
    }
}
