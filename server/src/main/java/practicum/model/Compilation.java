package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "compilations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title")
    private String title;
    @ManyToMany
    @JoinTable(name = "events_in_compilation", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;

    public Compilation(boolean pinned, String title) {
        this.pinned = pinned;
        this.title = title;
    }
}