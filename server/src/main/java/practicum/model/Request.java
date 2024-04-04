package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
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
    @Column(name = "status")
    private String status;

    public Request(LocalDateTime createdDate, Event event, User requester, String status) {
        this.createdDate = createdDate;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
