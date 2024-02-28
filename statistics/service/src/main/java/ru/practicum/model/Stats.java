package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String app;
    @Column
    private String uri;
    @Column
    private String ip;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
