package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "title")
    private String title;
    @Column(name = "creation_date")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Column(name = "state")
    private String state;

    public Event(String annotation, Category category,
                 String description, LocalDateTime eventDate,
                 Location location, boolean paid,
                 int participantLimit, boolean moderation,
                 String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = moderation;
        this.title = title;
    }
}
