package com.nadila.MegaCityCab.service.AuthService;

import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CabUserDetailsService implements UserDetailsService {

    private final CabUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CabUser cabUser =  Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFound("User not found!"));
        return new CabUserDetails(cabUser);
    }
}
