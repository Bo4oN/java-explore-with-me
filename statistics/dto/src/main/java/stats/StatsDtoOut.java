package stats;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatsDtoOut {
    private String app;
    private String uri;
    private int hits;

    public StatsDtoOut(String app, String uri, int hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    //String getApp();
//
    //String getUri();
//
    //Integer getHits();
}
