package com.seven.RailroadApp.repositories;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.enums.StateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, LocationId>{
    Optional<Location> findByStateCodeAndStationNo(StateCode stateCode, String stationNo);
    Boolean deleteByStateCodeAndStationNo(StateCode stateCode, String stationNo);
}
