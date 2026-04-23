package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @GetMapping("/list")
    public String showPatientList(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "patient-list";
    }

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient) {
        patientRepository.save(patient);
        return "redirect:/reservations/new";
    }
}
