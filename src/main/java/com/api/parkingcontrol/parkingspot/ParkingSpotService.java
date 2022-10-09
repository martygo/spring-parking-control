package com.api.parkingcontrol.parkingspot;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.api.parkingcontrol.configs.exception.domain.DomainException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {
    final ParkingSpotRepository parkingSpotRepository;

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
        if (existsByLicensePlateCar(parkingSpotModel.getLicensePlateCar()))
            throw new DomainException("License plate car already parked");

        if (existsByParkingSpotNumber(parkingSpotModel.getParkingSpotNumber()))
            throw new DomainException("Parking spot number already parked");

        if (existsByApartmentAndBlock(parkingSpotModel.getApartment(), parkingSpotModel.getBlock()))
            throw new DomainException("Apartment and block already parked");

        return parkingSpotRepository.save(parkingSpotModel);
    }

    public Page<ParkingSpotModel> findAll(int number, int size) {
        PageRequest pageable = PageRequest.of(number - 1, size);

        return parkingSpotRepository.findAll(pageable);
    }

    public Optional<ParkingSpotModel> findOne(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel) {
        parkingSpotRepository.delete(parkingSpotModel);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }
}
