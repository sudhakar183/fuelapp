package com.dmf.fuelapp.service;

import com.dmf.fuelapp.model.FuelEntry;
import com.dmf.fuelapp.model.FuelEntryWithTotalPrice;
import com.dmf.fuelapp.model.FuelStatistics;
import com.dmf.fuelapp.repository.FuelEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuelConsumptionService {

    private final FuelEntryRepository fuelEntryRepository;

    @Autowired
    public FuelConsumptionService(FuelEntryRepository fuelEntryRepository) {
        this.fuelEntryRepository = fuelEntryRepository;
    }

    public List<FuelEntry> getAll() {
        return (ArrayList<FuelEntry>) fuelEntryRepository.findAll();
    }

    public List<FuelEntry> getAllByDriverId(int driverId) {
        return fuelEntryRepository.findAllByDriverId(driverId);
    }

    public FuelEntry getFuelEntry(Long id) {
        return fuelEntryRepository.findById(id).orElse(null);
    }

    public void add (FuelEntry entry) {
        fuelEntryRepository.save(entry);
    }

    public void addAll (List<FuelEntry> entries) {
        fuelEntryRepository.saveAll(entries);
    }

    public void delete (Long id) {
        fuelEntryRepository.deleteById(id);
    }

    public Map<Month, Double> getTotalCostGroupedByMonth() {
        return getAll().stream().collect(
                Collectors.groupingBy(FuelEntry::retrieveMonth, Collectors.summingDouble(FuelEntry::getPrice)));
    }

    public Map<Month, Double> getTotalCostForDriverGroupedByMonth(int driverId) {
        return getAllByDriverId(driverId).stream().collect(
                Collectors.groupingBy(FuelEntry::retrieveMonth, Collectors.summingDouble(FuelEntry::getPrice)));
    }

    public List<FuelEntryWithTotalPrice> getFuelConsumptionForDriverByMonth(int driverId, Month month) {
        List<FuelEntry> entries = getAllByDriverId(driverId).stream().collect(Collectors.groupingBy(FuelEntry::retrieveMonth)).get(month);
        if(entries != null && !entries.isEmpty()) {
            double sum = entries.stream().mapToDouble(FuelEntry::getPrice).sum();
            return getFuelEntriesWithTotalPrice(entries, sum);
        } else {
            return Collections.emptyList();
        }
    }

    public List<FuelEntryWithTotalPrice> getFuelConsumptionByMonth(Month month) {
        List<FuelEntry> entries = getAll().stream().collect(Collectors.groupingBy(FuelEntry::retrieveMonth)).get(month);
        if(entries != null && !entries.isEmpty()) {
            double sum = entries.stream().mapToDouble(FuelEntry::getPrice).sum();
            return getFuelEntriesWithTotalPrice(entries, sum);
        } else {
            return Collections.emptyList();
        }
    }

    public Map<Month, FuelStatistics> getFuelStatisticsByFuelType(FuelEntry.FuelType fuelType) {
        List<FuelEntry> entries = getAll().stream().collect(Collectors.groupingBy(FuelEntry::getFuelType)).get(fuelType);
        if(entries != null && !entries.isEmpty())
            return getFuelStatistics(entries.stream().collect(Collectors.groupingBy(FuelEntry::retrieveMonth)), fuelType);
        return Collections.emptyMap();
    }

    public Map<Month, FuelStatistics> getFuelStatisticsForDriverByFuelType(int driverId, FuelEntry.FuelType fuelType) {
        List<FuelEntry> entries = getAllByDriverId(driverId).stream()
                .collect(Collectors.groupingBy(FuelEntry::getFuelType)).get(fuelType);
        if(entries != null && !entries.isEmpty())
            return getFuelStatistics(entries.stream().collect(Collectors.groupingBy(FuelEntry::retrieveMonth)), fuelType);
        return Collections.emptyMap();
    }

    private List<FuelEntryWithTotalPrice> getFuelEntriesWithTotalPrice(List<FuelEntry> entries, double sum) {
        List<FuelEntryWithTotalPrice> updatedEntries = new ArrayList<>();
        entries.forEach(fuelEntry -> updatedEntries.add(new FuelEntryWithTotalPrice(fuelEntry, sum)));
        return updatedEntries;
    }

    private Map<Month, FuelStatistics> getFuelStatistics(Map<Month, List<FuelEntry>> entriesMap, FuelEntry.FuelType fuelType) {
        if (entriesMap != null && !entriesMap.isEmpty()) {
            return entriesMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (value -> {

                double sum = value.getValue().stream().mapToDouble(FuelEntry::getPrice).sum();
                double volume = value.getValue().stream().mapToDouble(FuelEntry::getVolume).sum();
                double average = 0;
                OptionalDouble averageOptional = value.getValue().stream().mapToDouble(FuelEntry::getPrice).average();
                if (averageOptional.isPresent())
                    average = averageOptional.getAsDouble();

                return new FuelStatistics(fuelType, volume, average, sum);
            })));
        } else {
            return Collections.emptyMap();
        }
    }
}
