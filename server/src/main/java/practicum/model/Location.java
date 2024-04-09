package practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "location_lat")
    private float lat;
    @Column(name = "location_lon")
    private float lon;

    public Location(float latitude, float longitude) {
        this.lat = latitude;
        this.lon = longitude;
    }
}
