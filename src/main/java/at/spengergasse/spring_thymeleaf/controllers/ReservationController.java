package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;
    private final BodyRegionRepository bodyRegionRepository;

    public ReservationController(ReservationRepository reservationRepository, PatientRepository patientRepository, DeviceRepository deviceRepository, BodyRegionRepository bodyRegionRepository) {
        this.reservationRepository = reservationRepository;
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
        this.bodyRegionRepository = bodyRegionRepository;
    }

    @GetMapping("/new")
    public String showReservationForm(@RequestParam(required = false) Integer patientId, Model model) {
        Reservation reservation = new Reservation();
        if (patientId != null) {
            patientRepository.findById(patientId).ifPresent(reservation::setPatient);
        }
        model.addAttribute("reservation", reservation);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("bodyRegions", bodyRegionRepository.findAll());
        return "reservation-form";
    }

    @PostMapping("/new")
    public String createReservation(@Valid @ModelAttribute Reservation reservation, BindingResult bindingResult, Model model) {
        if (reservation.getStartTime() != null && reservation.getEndTime() != null) {
            if (reservation.getEndTime().isBefore(reservation.getStartTime())) {
                bindingResult.rejectValue("endTime", "error.reservation", "Die Endzeit muss nach der Startzeit liegen");
            }

            if (reservation.getDevice() != null) {
                List<Reservation> overlappingDevice = reservationRepository.findOverlappingWithDevice(
                        reservation.getDevice().getId(), reservation.getStartTime(), reservation.getEndTime());
                if (!overlappingDevice.isEmpty()) {
                    bindingResult.rejectValue("device", "error.reservation", "Das Gerät ist zu dieser Zeit bereits belegt");
                }
            }

            if (reservation.getPatient() != null) {
                List<Reservation> overlappingPatient = reservationRepository.findOverlappingWithPatient(
                        reservation.getPatient().getId(), reservation.getStartTime(), reservation.getEndTime());
                if (!overlappingPatient.isEmpty()) {
                    bindingResult.rejectValue("patient", "error.reservation", "Der Patient hat zu dieser Zeit bereits einen Termin");
                }
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("devices", deviceRepository.findAll());
            model.addAttribute("bodyRegions", bodyRegionRepository.findAll());
            return "reservation-form";
        }
        reservationRepository.save(reservation);
        return "redirect:/reservations/list?deviceId=" + reservation.getDevice().getId();
    }

    @GetMapping("/list")
    public String showReservationList(@RequestParam(required = false) Integer deviceId, Model model) {
        List<Device> devices = deviceRepository.findAll();
        model.addAttribute("devices", devices);
        
        List<Reservation> reservations;
        if (deviceId != null) {
            reservations = reservationRepository.findByDeviceIdOrderByStartTimeAsc(deviceId);
            model.addAttribute("selectedDeviceId", deviceId);
        } else {
            reservations = reservationRepository.findAll();
        }
        model.addAttribute("reservations", reservations);
        
        return "reservation-list";
    }
}
