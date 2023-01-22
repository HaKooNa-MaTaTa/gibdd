package com.igonincompany.gibdd.repositories;

import com.igonincompany.gibdd.models.CarNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NumberRepository extends JpaRepository<CarNumber, Long> {
    @Query(value = "select * from car_number order by create_date desc limit 1",
            nativeQuery = true)
    Optional<CarNumber> getLastNumber();
}
