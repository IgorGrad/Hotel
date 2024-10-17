package hr.lemax.hotel.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class SoftDeletedModelBase {
    public final static String FIELD_NAME_DELETED = "removed";
    public final static String FIELD_NAME_REMOVED_BY = "removedBy";


    @Column(columnDefinition = "timestamp with time zone")
    protected LocalDateTime removed;

    @Column
    protected Long removedBy;

    public boolean isDeleted() {
        return removed != null;
    }

    public void markAsDeleted(Long removedBy) {
        this.removed = LocalDateTime.now();
        this.removedBy = removedBy;
    }
}
