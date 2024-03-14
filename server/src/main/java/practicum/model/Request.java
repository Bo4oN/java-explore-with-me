package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import practicum.model.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "created")
    private LocalDateTime createdDate;
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester")
    private User requester;
    @Enumerated(EnumType.STRING)
    @Type(type = "request_status")
    private Status status;

    public Request(LocalDateTime createdDate, Event event, User requester, Status status) {
        this.createdDate = createdDate;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
