package hr.lemax.hotel.repository;

import hr.lemax.hotel.model.Hotel;
import hr.lemax.hotel.repository.base.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends
        SoftDeleteRepository<Hotel, Long>,
        JpaSpecificationExecutor<Hotel> {
}
