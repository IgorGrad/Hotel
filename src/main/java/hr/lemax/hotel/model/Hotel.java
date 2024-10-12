package hr.lemax.hotel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Hotel {

    @Id
    private Long id;
    private String name;
    private double price;
    // X axis
    private double longitude;
    // Y axis
    private double latitude;

    private double distance;

    public Hotel(Long id, String name, double price, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
