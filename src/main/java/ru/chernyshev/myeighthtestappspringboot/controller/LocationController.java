package ru.chernyshev.myeighthtestappspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.chernyshev.myeighthtestappspringboot.dto.LocationForm;
import ru.chernyshev.myeighthtestappspringboot.service.LocationService;

import javax.validation.Valid;

@Controller
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public String list(Model model, Authentication auth) {
        model.addAttribute("locations", locationService.findVisible(auth));
        return "locations/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("locationForm", new LocationForm());
        model.addAttribute("title", "Новая площадка");
        model.addAttribute("action", "/locations");
        return "locations/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("locationForm") LocationForm form, BindingResult br,
                         Model model, Authentication auth, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("title", "Новая площадка");
            model.addAttribute("action", "/locations");
            return "locations/form";
        }
        locationService.create(form, auth);
        ra.addFlashAttribute("message", "Площадка добавлена");
        return "redirect:/locations";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute("locationForm", LocationForm.from(locationService.getForEdit(id, auth)));
        model.addAttribute("title", "Изменение площадки");
        model.addAttribute("action", "/locations/" + id);
        return "locations/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("locationForm") LocationForm form,
                         BindingResult br, Model model, Authentication auth, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("title", "Изменение площадки");
            model.addAttribute("action", "/locations/" + id);
            return "locations/form";
        }
        locationService.update(id, form, auth);
        ra.addFlashAttribute("message", "Площадка обновлена");
        return "redirect:/locations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        try {
            locationService.delete(id, auth);
            ra.addFlashAttribute("message", "Площадка удалена");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/locations";
    }
}
