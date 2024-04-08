package stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDtoOut {
    private String app;
    private String uri;
    private int hits;

    public StatsDtoOut(String app, String uri, String hits) {
        this.app = app;
        this.uri = uri;
        this.hits = Integer.parseInt(hits);
    }

    public StatsDtoOut(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = Integer.parseInt(String.valueOf(hits));
    }
}
