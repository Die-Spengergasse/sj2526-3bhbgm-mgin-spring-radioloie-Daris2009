package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

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

    @GetMapping("/add")
    public String addForm(Model model, @RequestParam(name = "patientId", required = false) Integer patientId) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("regions", bodyRegionRepository.findAll());
        model.addAttribute("preselectedPatientId", patientId);
        return "add_reservation";
    }

    @PostMapping("/add")
    public String handleAdd(Model model,
                            @RequestParam("patientId") Integer patientId,
                            @RequestParam("deviceId") Integer deviceId,
                            @RequestParam("bodyRegionId") Integer bodyRegionId,
                            @RequestParam("startTimeStr") String startTimeStr,
                            @RequestParam("endTimeStr") String endTimeStr,
                            @RequestParam(value = "comment", required = false) String comment) {
        try {
            // basic validations
            if (patientId == null || deviceId == null || bodyRegionId == null) {
                model.addAttribute("error","Bitte Patient, Gerät und Körperregion auswählen.");
                return addForm(model, patientId);
            }

            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            Optional<BodyRegion> regionOpt = bodyRegionRepository.findById(bodyRegionId);

            if (patientOpt.isEmpty() || deviceOpt.isEmpty() || regionOpt.isEmpty()) {
                model.addAttribute("error","Ausgewählte Entität nicht gefunden.");
                return addForm(model, patientId);
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startTime;
            LocalDateTime endTime;
            try {
                startTime = LocalDateTime.parse(startTimeStr, fmt);
                endTime = LocalDateTime.parse(endTimeStr, fmt);
            } catch (DateTimeParseException ex) {
                log.warn("Fehler beim Parsen der Zeit: start='{}' end='{}'", startTimeStr, endTimeStr, ex);
                model.addAttribute("error", "Ungültiges Datums-/Zeitformat. Bitte gültige Start- und Endzeit wählen.");
                return addForm(model, patientId);
            }

            if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
                model.addAttribute("error","Endzeit muss nach der Startzeit liegen.");
                return addForm(model, patientId);
            }

            // Check overlapping
            List<Reservation> overlaps = reservationRepository.findOverlapping(deviceId, startTime, endTime);
            if (!overlaps.isEmpty()) {
                model.addAttribute("error","Die gewählte Zeit überlappt bereits bestehende Reservierungen.");
                return addForm(model, patientId);
            }

            Reservation reservation = new Reservation();
            reservation.setPatient(patientOpt.get());
            reservation.setDevice(deviceOpt.get());
            reservation.setBodyRegion(regionOpt.get());
            reservation.setStartTime(startTime);
            reservation.setEndTime(endTime);
            reservation.setComment(comment);

            reservationRepository.save(reservation);
            // Redirect to patient list per user's request
            return "redirect:/patient/list";
        } catch (Exception e) {
            // Log for debugging and show a friendly error in the form
            log.error("Fehler beim Speichern der Reservierung", e);
            model.addAttribute("error", "Fehler beim Speichern der Reservierung: " + e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            model.addAttribute("errorStacktrace", sw.toString());
            return addForm(model, patientId);
        }
    }

    @GetMapping("/list")
    public String listForDevice(@RequestParam(name = "deviceId", required = false) Integer deviceId, Model model) {
        try {
            List<Device> devices = deviceRepository.findAll();
            model.addAttribute("devices", devices);
            if (deviceId != null) {
                model.addAttribute("reservations", reservationRepository.findByDeviceIdOrderByStartTimeAsc(deviceId));
                model.addAttribute("selectedDeviceId", deviceId);
            } else {
                // show all reservations when no device selected
                model.addAttribute("reservations", reservationRepository.findAll());
            }
            return "reslist";
        } catch (Exception e) {
            log.error("Fehler beim Laden der Reservierungs-Liste", e);
            model.addAttribute("devices", deviceRepository.findAll());
            model.addAttribute("reservations", java.util.Collections.emptyList());
            model.addAttribute("error", "Fehler beim Laden der Reservierungs-Liste: " + e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            model.addAttribute("errorStacktrace", sw.toString());
            return "reslist";
        }
    }
}
