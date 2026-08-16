package com.worksaathi.service;

import com.worksaathi.dto.auth.*;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.ResourceNotFoundException;
import com.worksaathi.repository.UserRepository;
import com.worksaathi.repository.WorkerRepository;
import com.worksaathi.security.CustomUserDetailsService;
import com.worksaathi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already registered");
        }

        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be CUSTOMER or WORKER");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setIsActive(true);
        user.setIsVerified(false);

        user = userRepository.save(user);

        // If role is WORKER, create worker profile
        if (role == User.Role.WORKER) {
            Worker worker = new Worker();
            worker.setUser(user);
            worker.setVerificationStatus(Worker.VerificationStatus.PENDING);
            worker.setAvailabilityStatus(Worker.AvailabilityStatus.AVAILABLE);
            worker.setAverageRating(0.0);
            worker.setTotalReviews(0);
            workerRepository.save(worker);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                user.getId(),
                user.getRole().name(),
                user.getName(),
                user.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        if (!user.getIsActive()) {
            throw new BadRequestException("Account is inactive");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                user.getId(),
                user.getRole().name(),
                user.getName(),
                user.getEmail()
        );
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (jwtService.isTokenValid(request.getRefreshToken(), userDetails)) {
            String accessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

            return new AuthResponse(
                    accessToken,
                    newRefreshToken,
                    "Bearer",
                    user.getId(),
                    user.getRole().name(),
                    user.getName(),
                    user.getEmail()
            );
        }

        throw new BadRequestException("Invalid refresh token");
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        // TODO: Implement password reset logic with email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        // Generate reset token and send email
        // For now, just acknowledge the request
    }

    public void resetPassword(ResetPasswordRequest request) {
        // TODO: Implement password reset with token validation
        // For now, this is a placeholder
    }
}
