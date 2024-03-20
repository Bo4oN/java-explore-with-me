package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "location_lat")
    private float latitude;
    @Column(name = "location_lon")
    private float longitude;

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
