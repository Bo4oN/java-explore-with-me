package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "subs")
@IdClass(SubId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {
    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Id
    @Column(name = "sub_id", nullable = false)
    private long subId;
}
