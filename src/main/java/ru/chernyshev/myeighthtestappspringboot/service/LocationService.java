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
import ru.chernyshev.myeighthtestappspringboot.dto.LocationForm;
import ru.chernyshev.myeighthtestappspringboot.model.Location;
import ru.chernyshev.myeighthtestappspringboot.repository.LocationRepository;
import ru.chernyshev.myeighthtestappspringboot.repository.ServerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private static final Logger actions = LoggerFactory.getLogger("user.actions");

    private final LocationRepository locationRepository;
    private final ServerRepository serverRepository;
    private final AccessService access;

    public List<Location> findVisible(Authentication a) {
        return access.seesOnlyOwn(a)
                ? locationRepository.findByCreatedByOrderByIdAsc(a.getName())
                : locationRepository.findAllByOrderByIdAsc();
    }

    /** Возвращает площадку, если она видна текущему пользователю. */
    public Location getVisible(Long id, Authentication a) {
        Location l = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Площадка не найдена"));
        if (access.seesOnlyOwn(a) && !l.getCreatedBy().equals(a.getName())) {
            throw new AccessDeniedException("Нет доступа к чужой площадке");
        }
        return l;
    }

    private Location getModifiable(Long id, Authentication a) {
        Location l = getVisible(id, a);
        if (!access.canModify(a, l.getCreatedBy())) {
            throw new AccessDeniedException("Нет прав на изменение площадки");
        }
        return l;
    }

    public Location getForEdit(Long id, Authentication a) {
        return getModifiable(id, a);
    }

    @Transactional
    public Location create(LocationForm form, Authentication a) {
        if (!access.canCreate(a)) {
            throw new AccessDeniedException("Нет прав на создание");
        }
        Location l = new Location();
        form.applyTo(l);
        l.setCreatedBy(a.getName());
        locationRepository.save(l);
        actions.info("user={} action=CREATE_LOCATION id={} name={}", a.getName(), l.getId(), l.getName());
        return l;
    }

    @Transactional
    public void update(Long id, LocationForm form, Authentication a) {
        Location l = getModifiable(id, a);
        form.applyTo(l);
        locationRepository.save(l);
        actions.info("user={} action=UPDATE_LOCATION id={} name={}", a.getName(), l.getId(), l.getName());
    }

    @Transactional
    public void delete(Long id, Authentication a) {
        Location l = getModifiable(id, a);
        if (serverRepository.existsByLocationId(id)) {
            throw new IllegalStateException("Нельзя удалить площадку «" + l.getName()
                    + "»: на ней есть серверы. Сначала удалите или перенесите серверы.");
        }
        locationRepository.delete(l);
        actions.info("user={} action=DELETE_LOCATION id={} name={}", a.getName(), id, l.getName());
    }
}
