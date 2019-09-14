package com.dmf.fuelapp.repository;

import com.dmf.fuelapp.model.FuelEntry;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface FuelEntryRepository extends CrudRepository<FuelEntry, Long> {
    List<FuelEntry> findAllByDriverId(int driverId);
}
