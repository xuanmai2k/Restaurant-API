package com.r2s.sample;

import com.r2s.sample.user.entities.Role;
import com.r2s.sample.user.models.ERole;
import com.r2s.sample.user.repositories.RoleRepository;
import com.r2s.sample.utils.Constants;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {
    //    private final List<String> usernames =
//            List.of("ada.lovelace@nix.io", "alan.turing@nix.io", "dennis.ritchie@nix.io");
//    private final List<String> fullNames = List.of("Ada Lovelace", "Alan Turing", "Dennis Ritchie");
    private final List<ERole> roles = List.of(ERole.ROLE_ADMIN, ERole.ROLE_MODERATOR, ERole.ROLE_USER);
    //    private final String password = "Test12345_";
//
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        roles.forEach(this::initRole);
    }

    private void initRole(ERole eRole) {
        switch (eRole) {
            case ROLE_USER -> {
                saveRole(ERole.ROLE_USER);
            }
            case ROLE_MODERATOR -> {
                saveRole(ERole.ROLE_MODERATOR);
            }
            case ROLE_ADMIN -> {
                saveRole(ERole.ROLE_ADMIN);
            }
        }
    }

    private void saveRole(ERole eRole) {
        Optional<Role> role = roleRepository.findByName(eRole);
        if (role.isEmpty()) {
            Role userRole = new Role(eRole);
            roleRepository.save(userRole);
        }
    }
}
