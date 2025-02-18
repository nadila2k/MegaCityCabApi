package com.nadila.MegaCityCab.Data;

import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.model.Admin;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.repository.AdminRepository;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final CabUserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createAdmin();
    }


    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    public void createAdmin(){

        String email = "Admin@gmail.com";
        String password = "Admin";
        String userName = "Admin Manager";

        if (!userRepository.existsByEmail(email)) {
            CabUser cabUser = new CabUser();
            cabUser.setEmail(email);
            cabUser.setPassword(passwordEncoder.encode(password));
            cabUser.setRoles(Roles.ADMIN);

            CabUser adminUser =  userRepository.save(cabUser);

            Admin admin = new Admin();
            admin.setName(userName);
            admin.setCabUser(adminUser);
            adminRepository.save(admin);

            System.out.println("Admin user create ");

        }else {

            System.out.println("Admin user already exists. Skipping creation.");
        }

    }
}
