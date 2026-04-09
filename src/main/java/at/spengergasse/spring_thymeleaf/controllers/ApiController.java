package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import at.spengergasse.spring_thymeleaf.entities.ReservationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final DeviceRepository deviceRepository;
    private final PatientRepository patientRepository;
    private final ReservationRepository reservationRepository;

    public ApiController(DeviceRepository deviceRepository, PatientRepository patientRepository, ReservationRepository reservationRepository) {
        this.deviceRepository = deviceRepository;
        this.patientRepository = patientRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/devices")
    public List<?> devices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/patients")
    public List<?> patients() {
        return patientRepository.findAll();
    }

    @GetMapping("/reservations")
    public List<?> reservations(@RequestParam(name = "deviceId", required = false) Integer deviceId) {
        if (deviceId == null) {
            return reservationRepository.findAll();
        }
        return reservationRepository.findByDeviceIdOrderByStartTimeAsc(deviceId);
    }
}

