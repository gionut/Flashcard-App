package ro.ubbcluj.thesis.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubbcluj.thesis.domain.AppUser;
import ro.ubbcluj.thesis.repository.AppUserRepository;
import ro.ubbcluj.thesis.service.dto.AppUserDTO;
import ro.ubbcluj.thesis.service.mapper.AppUserMapper;

/**
 * Service Implementation for managing {@link AppUser}.
 */
@Service
@Transactional
public class AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    /**
     * Save a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    public AppUserDTO save(AppUserDTO appUserDTO) {
        log.debug("Request to save AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(appUser);
    }

    /**
     * Partially update a appUser.
     *
     * @param appUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppUserDTO> partialUpdate(AppUserDTO appUserDTO) {
        log.debug("Request to partially update AppUser : {}", appUserDTO);

        return appUserRepository
            .findById(appUserDTO.getId())
            .map(existingAppUser -> {
                appUserMapper.partialUpdate(existingAppUser, appUserDTO);

                return existingAppUser;
            })
            .map(appUserRepository::save)
            .map(appUserMapper::toDto);
    }

    /**
     * Get all the appUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUserDTO> findAll() {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAll().stream().map(appUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one appUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id).map(appUserMapper::toDto);
    }

    /**
     * Delete the appUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
