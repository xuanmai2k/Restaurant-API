package com.r2s.mobilestore;

import com.r2s.mobilestore.user.entities.Role;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.models.ERole;
import com.r2s.mobilestore.user.repositories.RoleRepository;
import com.r2s.mobilestore.user.repositories.UserRepository;
import com.r2s.mobilestore.utils.Constants;
import com.r2s.mobilestore.utils.Helpers;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<ERole> roles = List.of(ERole.ROLE_ADMIN, ERole.ROLE_USER);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        roles.forEach(this::initRole);
        initAdminUser(); // Gọi phương thức để tạo tài khoản admin
    }

    private void initAdminUser() {
        Optional<User> adminUser = Optional.ofNullable(userRepository.findByEmail("admin@example.com"));
        if (adminUser.isEmpty()) {
            // Nếu tài khoản admin chưa tồn tại, hãy tạo nó
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin")); // Mã hóa mật khẩu
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName(ERole.ROLE_ADMIN.toString()).orElseThrow());
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }
    }

    private void initRole(ERole eRole) {
        switch (eRole) {
            case ROLE_USER -> {
                saveRole(ERole.ROLE_USER.toString());
            }
            case ROLE_ADMIN -> {
                saveRole(ERole.ROLE_ADMIN.toString());
            }
        }
    }

    private void saveRole(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            Role userRole = new Role(name);
            roleRepository.save(userRole);
        }
    }
}
