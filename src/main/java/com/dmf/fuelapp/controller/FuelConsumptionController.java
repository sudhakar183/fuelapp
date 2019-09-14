package com.dmf.fuelapp.controller;

import com.dmf.fuelapp.model.FuelEntry;
import com.dmf.fuelapp.model.FuelEntryWithTotalPrice;
import com.dmf.fuelapp.model.FuelStatistics;
import com.dmf.fuelapp.service.FuelConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/fuelcontrol")
public class FuelConsumptionController {
    private final FuelConsumptionService fuelConsumptionService;

    @Autowired
    public FuelConsumptionController (FuelConsumptionService fuelConsumptionService) {
        this.fuelConsumptionService = fuelConsumptionService;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Input error. Check input fields")
    @ExceptionHandler({HttpMessageNotReadableException.class, DateTimeParseException.class})
    public void handleException() {
    }

    @GetMapping("entry")
    public List getAllEntries () {
        return fuelConsumptionService.getAll();
    }

    @PostMapping("entry")
    public ResponseEntity<String> addEntry(@RequestParam FuelEntry.FuelType fuelType, @RequestParam double volume,
                                           @RequestParam ("date") @DateTimeFormat(pattern="MM.dd.yyyy") String date,
                                           @RequestParam double price, @RequestParam int driverId) {
        fuelConsumptionService.add(new FuelEntry(fuelType, date, volume, price, driverId));
        return new ResponseEntity<>("Fuel entry saved", HttpStatus.OK);
    }

    @PostMapping(value = "entry/bulk", consumes =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addEntries(@RequestBody List<FuelEntry> fuelEntries) {
        fuelConsumptionService.addAll(fuelEntries);
        return new ResponseEntity<>("Fuel entries saved", HttpStatus.OK);
    }

    @GetMapping("costs")
    public Map<Month, Double> getFuelCostsGroupedByMonth () {
        return fuelConsumptionService.getTotalCostGroupedByMonth();
    }

    @GetMapping("costs/{driverId}")
    public Map<Month, Double> getFuelCostsForDriverGroupedByMonth (@PathVariable("driverId") int driverId) {
        return fuelConsumptionService.getTotalCostForDriverGroupedByMonth(driverId);
    }

    @GetMapping("consumption/{month}")
    public List<FuelEntryWithTotalPrice> getFuelConsumptionGroupedByMonth(@PathVariable("month") Month month) {
        return fuelConsumptionService.getFuelConsumptionByMonth(month);
    }

    @GetMapping("consumption/{month}/{driverId}")
    public List<FuelEntryWithTotalPrice> getFuelConsumptionForDriverGroupedByMonth (@PathVariable("month") Month month,
                                                                                    @PathVariable("driverId") int driverId) {
        return fuelConsumptionService.getFuelConsumptionForDriverByMonth(driverId, month);
    }

    @GetMapping("stats")
    public Map<Month, FuelStatistics> getFuelStats (@RequestParam("fuelType")FuelEntry.FuelType fuelType) {
        return fuelConsumptionService.getFuelStatisticsByFuelType(fuelType);
    }

    @GetMapping("stats/{driverId}")
    public Map<Month, FuelStatistics> getFuelStatsForDriver (@PathVariable("driverId") int driverId,
                                                             @RequestParam("fuelType")FuelEntry.FuelType fuelType) {
        return fuelConsumptionService.getFuelStatisticsForDriverByFuelType(driverId, fuelType);
    }
}
