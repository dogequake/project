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
import ru.chernyshev.myeighthtestappspringboot.dto.ServerForm;
import ru.chernyshev.myeighthtestappspringboot.service.LocationService;
import ru.chernyshev.myeighthtestappspringboot.service.ServerService;

import javax.validation.Valid;

@Controller
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;
    private final LocationService locationService;

    @GetMapping
    public String list(Model model, Authentication auth) {
        model.addAttribute("servers", serverService.findVisible(auth));
        return "servers/list";
    }

    @GetMapping("/new")
    public String newForm(Model model, Authentication auth) {
        prepareForm(model, auth, "Новый сервер", "/servers");
        model.addAttribute("serverForm", new ServerForm());
        return "servers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("serverForm") ServerForm form, BindingResult br,
                         Model model, Authentication auth, RedirectAttributes ra) {
        if (br.hasErrors()) {
            prepareForm(model, auth, "Новый сервер", "/servers");
            return "servers/form";
        }
        serverService.create(form, auth);
        ra.addFlashAttribute("message", "Сервер добавлен");
        return "redirect:/servers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute("serverForm", ServerForm.from(serverService.getForEdit(id, auth)));
        prepareForm(model, auth, "Изменение сервера", "/servers/" + id);
        return "servers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("serverForm") ServerForm form,
                         BindingResult br, Model model, Authentication auth, RedirectAttributes ra) {
        if (br.hasErrors()) {
            prepareForm(model, auth, "Изменение сервера", "/servers/" + id);
            return "servers/form";
        }
        serverService.update(id, form, auth);
        ra.addFlashAttribute("message", "Сервер обновлён");
        return "redirect:/servers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        serverService.delete(id, auth);
        ra.addFlashAttribute("message", "Сервер удалён");
        return "redirect:/servers";
    }

    private void prepareForm(Model model, Authentication auth, String title, String action) {
        model.addAttribute("title", title);
        model.addAttribute("action", action);
        model.addAttribute("locations", locationService.findVisible(auth));
    }
}
