package com.nadila.MegaCityCab.config;
import com.cloudinary.Cloudinary;
import com.nadila.MegaCityCab.config.jwt.AuthTokenFilter;
import com.nadila.MegaCityCab.service.AuthService.CabUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class CityCabConfig {
    private final CabUserDetailsService userDetailesService;
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dbiddrued");
        config.put("api_key", "367988313781747");
        config.put("api_secret", "c30rn4ORcvPGW_U5BsnTKChzeMc");
        return new Cloudinary(config);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailesService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // Added: Enable CORS by linking to the corsConfigurationSource bean
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Modified: Removed "/api/v1/vehicle/all/vehicles" from permitAll to require authentication
                        .requestMatchers("api/v1/auth/sign-in", "api/v1/auth/sign-up/passenger", "api/v1/auth/sign-up/driver", "api/v1/vehicle/all/vehicles/public").permitAll()
                        .anyRequest().authenticated()); // All other endpoints, including /api/v1/vehicle/all/vehicles, now require authentication
        httpSecurity.authenticationProvider(daoAuthenticationProvider());
        httpSecurity.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    // Added: Define CORS configuration to allow frontend requests from http://localhost:5173
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfig = new CorsConfiguration();
        // Added: Allow requests from your React frontend
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173")); // React app URL
        // Added: Allow all standard HTTP methods
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Added: Allow all headers, including Authorization for JWT
        corsConfig.setAllowedHeaders(List.of("*")); // Changed from specific headers to wildcard for flexibility
        // Added: Allow credentials (e.g., cookies or tokens) to be sent
        corsConfig.setAllowCredentials(true); // Important for authenticated requests
        var source = new UrlBasedCorsConfigurationSource();
        // Added: Apply this CORS config to all endpoints
        source.registerCorsConfiguration("/**", corsConfig); // Apply CORS to all paths
        return source;
    }
}