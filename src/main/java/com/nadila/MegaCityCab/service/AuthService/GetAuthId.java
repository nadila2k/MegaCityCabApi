package com.nadila.MegaCityCab.service.AuthService;

import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAuthId {

    private final CabUserRepository userRepository;

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            CabUser cabUser = userRepository.findByEmail(email);

            if (cabUser != null) {
                return cabUser.getId();
            } else {
                throw new ResourceNotFound("User not found with email: " + email);
            }
        } else {
            throw new ResourceNotFound("User authentication failed");
        }

    }
}
