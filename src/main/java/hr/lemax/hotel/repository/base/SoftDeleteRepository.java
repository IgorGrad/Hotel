package hr.lemax.hotel.repository.base;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface SoftDeleteRepository<T, ID> extends JpaRepository<T, ID> {

    /*@Override
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.removed = CURRENT_TIMESTAMP WHERE e.id = :id")
    void deleteById(@NonNull @Param("id") ID id);

    @Override
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.removed = CURRENT_TIMESTAMP WHERE e = :entity")
    void delete(@NonNull @Param("entity") T entity);

    @Override
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.removed = CURRENT_TIMESTAMP WHERE e.id IN (:ids)")
    void deleteAllById(@NonNull @Param("ids") Iterable<? extends ID> entities);

    @Override
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.removed = CURRENT_TIMESTAMP WHERE e IN (:entities)")
    void deleteAll(@NonNull @Param("entities") Iterable<? extends T> entities);

    @Override
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.removed = CURRENT_TIMESTAMP")
    void deleteAll();*/
}
