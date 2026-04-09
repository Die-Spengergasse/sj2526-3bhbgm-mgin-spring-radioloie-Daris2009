package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/device")
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        // redirect to patient list instead of providing a device UI
        return "redirect:/patient/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return "redirect:/patient/list";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute("device") Object device) {
        return "redirect:/patient/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        return "redirect:/patient/list";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") Integer id, @ModelAttribute("device") Object device) {
        return "redirect:/patient/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        return "redirect:/patient/list";
    }
}
