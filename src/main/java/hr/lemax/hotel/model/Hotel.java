package hr.lemax.hotel.model;

import hr.lemax.hotel.common.aspect.SoftDeleteAspect;
import hr.lemax.hotel.model.base.SoftDeletedModelBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotel", schema = "general", indexes = {
        @Index(name = "ix_hotel_longitude", columnList = "longitude"),
        @Index(name = "ix_hotel_latitude", columnList = "latitude")
})
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "softDeleteFilter", condition = "removed IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Hotel extends SoftDeletedModelBase {
    public final static String FIELD_NAME_ID = "id";
    public final static String FIELD_NAME_NAME = "name";
    public final static String FIELD_NAME_PRICE = "price";
    public final static String FIELD_NAME_LONGITUDE = "longitude";
    public final static String FIELD_NAME_FIRST_NAME = "latitude";

    public final static String FIELD_NAME_CREATED = "created";
    public final static String FIELD_NAME_CREATED_BY = "createdBy";
    public final static String FIELD_NAME_UPDATED = "updated";
    public final static String FIELD_NAME_UPDATED_BY = "updatedBy";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length = 512)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    protected LocalDateTime created;

    @Column
    protected Long createdBy;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    protected LocalDateTime updated;

    @Column
    protected Long updatedBy;

    @Transient
    private double distance;

    public Hotel(
            Long id,
            String name,
            double price,
            double longitude,
            double latitude) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @PrePersist
    public void doPrePersist() {
        created = updated = LocalDateTime.now();
        //createdBy = updatedBy = RequestContextHelper.getCurrentUserId();
    }

    @PreUpdate
    public void doPreUpdate() {
        updated = LocalDateTime.now();
        //updatedBy = RequestContextHelper.getCurrentUserId();
    }
}
