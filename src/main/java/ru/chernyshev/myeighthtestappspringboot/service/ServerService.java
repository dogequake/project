package ru.chernyshev.myeighthtestappspringboot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.chernyshev.myeighthtestappspringboot.dto.ServerForm;
import ru.chernyshev.myeighthtestappspringboot.model.Location;
import ru.chernyshev.myeighthtestappspringboot.model.Server;
import ru.chernyshev.myeighthtestappspringboot.repository.ServerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {

    private static final Logger actions = LoggerFactory.getLogger("user.actions");

    private final ServerRepository serverRepository;
    private final LocationService locationService;
    private final AccessService access;

    public List<Server> findVisible(Authentication a) {
        return access.seesOnlyOwn(a)
                ? serverRepository.findByCreatedByOrderByIdAsc(a.getName())
                : serverRepository.findAllByOrderByIdAsc();
    }

    public Server getVisible(Long id, Authentication a) {
        Server s = serverRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сервер не найден"));
        if (access.seesOnlyOwn(a) && !s.getCreatedBy().equals(a.getName())) {
            throw new AccessDeniedException("Нет доступа к чужому серверу");
        }
        return s;
    }

    private Server getModifiable(Long id, Authentication a) {
        Server s = getVisible(id, a);
        if (!access.canModify(a, s.getCreatedBy())) {
            throw new AccessDeniedException("Нет прав на изменение сервера");
        }
        return s;
    }

    public Server getForEdit(Long id, Authentication a) {
        return getModifiable(id, a);
    }

    @Transactional
    public Server create(ServerForm form, Authentication a) {
        if (!access.canCreate(a)) {
            throw new AccessDeniedException("Нет прав на создание");
        }
        Location location = locationService.getVisible(form.getLocationId(), a);
        Server s = new Server();
        form.applyTo(s);
        s.setLocation(location);
        s.setCreatedBy(a.getName());
        serverRepository.save(s);
        actions.info("user={} action=CREATE_SERVER id={} name={}", a.getName(), s.getId(), s.getName());
        return s;
    }

    @Transactional
    public void update(Long id, ServerForm form, Authentication a) {
        Server s = getModifiable(id, a);
        Location location = locationService.getVisible(form.getLocationId(), a);
        form.applyTo(s);
        s.setLocation(location);
        serverRepository.save(s);
        actions.info("user={} action=UPDATE_SERVER id={} name={}", a.getName(), s.getId(), s.getName());
    }

    @Transactional
    public void delete(Long id, Authentication a) {
        Server s = getModifiable(id, a);
        serverRepository.delete(s);
        actions.info("user={} action=DELETE_SERVER id={} name={}", a.getName(), id, s.getName());
    }
}
