package com.dmf.fuelapp.service;

import com.dmf.fuelapp.model.FuelEntry;
import com.dmf.fuelapp.repository.FuelEntryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FuelConsumptionServiceTest {
    private static ArrayList<FuelEntry> entries = new ArrayList<>();
    private static ArrayList<FuelEntry> entriesForDriver1 = new ArrayList<>();
    private static FuelEntry entry1 = new FuelEntry(FuelEntry.FuelType.A95, "02.21.2018", 10, 110, 1);
    private static FuelEntry entry2 = new FuelEntry(FuelEntry.FuelType.A95, "02.22.2018", 10, 110, 1);
    private static FuelEntry entry3 = new FuelEntry(FuelEntry.FuelType.A95, "02.21.2018", 10, 110, 2);
    private static FuelEntry entry4 = new FuelEntry(FuelEntry.FuelType.A95, "03.21.2018", 10, 110, 1);
    private static Map<Month, Double> costs = new HashMap<>();
    private static Map<Month, Double> costsForDriver1 = new HashMap<>();

    @TestConfiguration
    static class FuelConsumptionServiceTestContextConfiguration {
        @MockBean
        private static FuelEntryRepository fuelEntryRepository;

        @Bean
        public FuelConsumptionService fuelConsumptionService() {
            entries.add(entry1);
            entries.add(entry2);
            entries.add(entry3);
            entries.add(entry4);
            entriesForDriver1.add(entry1);
            entriesForDriver1.add(entry2);
            entriesForDriver1.add(entry4);
            costs.put(Month.FEBRUARY, 330d);
            costs.put(Month.MARCH, 110d);
            costsForDriver1.put(Month.FEBRUARY, 220d);
            costsForDriver1.put(Month.MARCH, 110d);
            when(fuelEntryRepository.findAll()).thenReturn(entries);

            return new FuelConsumptionService(fuelEntryRepository);
        }
    }

    @Autowired
    private FuelConsumptionService fuelConsumptionService;

    @Before
    public void setUp() {
        when(fuelConsumptionService.getAll()).thenReturn(entries);
        when(fuelConsumptionService.getAllByDriverId(1)).thenReturn(entriesForDriver1);
    }

    @Test
    public void whenGetTotalCostsGroupedByMonth_ThenReturnCorrectMap() {
        assert(fuelConsumptionService.getTotalCostGroupedByMonth()).equals(costs);
    }

    @Test
    public void whenGetTotalCostForDriverGroupedByMonth_ThenReturnCorrectMap() {
        assert(fuelConsumptionService.getTotalCostForDriverGroupedByMonth(1)).equals(costsForDriver1);
    }

    @Test
    public void whenGetFuelConsumptionByMonth_ThenReturnCorrectResults() {
        assert((fuelConsumptionService.getFuelConsumptionByMonth(Month.FEBRUARY)).size() == 3);
        assert((fuelConsumptionService.getFuelConsumptionByMonth(Month.FEBRUARY)).get(0).getTotalPrice() == 330d);
        assert((fuelConsumptionService.getFuelConsumptionByMonth(Month.FEBRUARY)).get(1).getTotalPrice() == 330d);
        assert((fuelConsumptionService.getFuelConsumptionByMonth(Month.FEBRUARY)).get(1).getPrice() == 110d);
        assert((fuelConsumptionService.getFuelConsumptionByMonth(Month.FEBRUARY)).get(2)
                .getFuelType().equals(FuelEntry.FuelType.A95));
    }

    @Test
    public void whenGetFuelConsumptionForDriverByMonth_ThenReturnResults() {
        assert((fuelConsumptionService.getFuelConsumptionForDriverByMonth(1, Month.FEBRUARY)).size() == 2);
        assert((fuelConsumptionService.getFuelConsumptionForDriverByMonth(1, Month.FEBRUARY)).get(0)
                .getTotalPrice() == 220d);
        assert((fuelConsumptionService.getFuelConsumptionForDriverByMonth(1, Month.FEBRUARY))
                .get(1).getTotalPrice() == 220d);
        assert((fuelConsumptionService.getFuelConsumptionForDriverByMonth(1, Month.FEBRUARY))
                .get(1).getPrice() == 110d);
        assert((fuelConsumptionService.getFuelConsumptionForDriverByMonth(1, Month.FEBRUARY))
                .get(1).getFuelType().equals(FuelEntry.FuelType.A95));
    }

    @Test
    public void whenGetFuelStatisticsByFuelType_ThenReturnCorrectResults () {
        assert((fuelConsumptionService.getFuelStatisticsByFuelType(FuelEntry.FuelType.A95)).size() == 2);
        assert((fuelConsumptionService.getFuelStatisticsByFuelType(FuelEntry.FuelType.A95)).get(Month.FEBRUARY)
                .getTotalPrice() == 330d);
        assert((fuelConsumptionService.getFuelStatisticsByFuelType(FuelEntry.FuelType.A95)).get(Month.MARCH)
                .getTotalPrice() == 110d);
        assert((fuelConsumptionService.getFuelStatisticsByFuelType(FuelEntry.FuelType.A95)).get(Month.FEBRUARY)
                .getVolume() == 30d);
    }

    @Test
    public void whenGetFuelStatisticsForDriverByFuelType_ThenReturnCorrectResults () {
        assert((fuelConsumptionService.getFuelStatisticsForDriverByFuelType(1, FuelEntry.FuelType.A95)).size() == 2);
        assert((fuelConsumptionService.getFuelStatisticsForDriverByFuelType(1, FuelEntry.FuelType.A95)).get(Month.FEBRUARY)
                .getTotalPrice() == 220d);
        assert((fuelConsumptionService.getFuelStatisticsForDriverByFuelType(1, FuelEntry.FuelType.A95)).get(Month.FEBRUARY)
                .getVolume() == 20d);
    }

    @Test
    public void whenGetFuelConsumptionByMonthAndNoCorrespondingEntries_ThenReturnEmptyMap() {
        when(fuelConsumptionService.getAll()).thenReturn(new ArrayList<>());
        assert(fuelConsumptionService.getFuelConsumptionByMonth(Month.FEBRUARY)).equals(Collections.emptyList());
    }

    @Test
    public void whenGetFuelConsumptionForDriverByMonthAndNoCorrespondingEntries_ThenReturnEmptyMap() {
        when(fuelConsumptionService.getAllByDriverId(1)).thenReturn(new ArrayList<>());
        assert(fuelConsumptionService.getFuelConsumptionForDriverByMonth(1, Month.FEBRUARY)).equals(Collections.emptyList());
    }

    @Test
    public void whenGetFuelStatisticsByFuelTypeAndNoCorrespondingEntries_ThenReturnEmptyMap () {
        assert(fuelConsumptionService.getFuelStatisticsByFuelType(FuelEntry.FuelType.D)).equals(Collections.emptyMap());
    }

    @Test
    public void whenGetFuelStatisticsForDriverByFuelTypeAndNoCorrespondingEntries_ThenReturnEmptyMap () {
        assert(fuelConsumptionService.getFuelStatisticsForDriverByFuelType(1, FuelEntry.FuelType.D)
                .equals(Collections.emptyMap()));
    }
}
