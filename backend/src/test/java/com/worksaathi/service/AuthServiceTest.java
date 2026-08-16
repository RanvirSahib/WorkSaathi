package com.worksaathi.service;

import com.worksaathi.dto.auth.AuthResponse;
import com.worksaathi.dto.auth.LoginRequest;
import com.worksaathi.dto.auth.RegisterRequest;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.repository.UserRepository;
import com.worksaathi.repository.WorkerRepository;
import com.worksaathi.security.CustomUserDetailsService;
import com.worksaathi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private User sampleCustomer;
    private User sampleWorkerUser;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        sampleCustomer = new User();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("Customer User");
        sampleCustomer.setEmail("customer@test.com");
        sampleCustomer.setPhone("9876543210");
        sampleCustomer.setPasswordHash("hashedPassword");
        sampleCustomer.setRole(User.Role.CUSTOMER);
        sampleCustomer.setIsActive(true);

        sampleWorkerUser = new User();
        sampleWorkerUser.setId(2L);
        sampleWorkerUser.setName("Worker User");
        sampleWorkerUser.setEmail("worker@test.com");
        sampleWorkerUser.setPhone("9876543211");
        sampleWorkerUser.setPasswordHash("hashedPassword");
        sampleWorkerUser.setRole(User.Role.WORKER);
        sampleWorkerUser.setIsActive(true);

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("customer@test.com")
                .password("hashedPassword")
                .roles("CUSTOMER")
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new customer")
    void testRegisterCustomerSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Customer User");
        request.setEmail("customer@test.com");
        request.setPassword("password123");
        request.setPhone("9876543210");
        request.setRole("CUSTOMER");

        when(userRepository.existsByEmail("customer@test.com")).thenReturn(false);
        when(userRepository.existsByPhone("9876543210")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleCustomer);
        when(userDetailsService.loadUserByUsername("customer@test.com")).thenReturn(userDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("mock-access-token");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("mock-refresh-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock-access-token", response.getAccessToken());
        assertEquals("mock-refresh-token", response.getRefreshToken());
        assertEquals("Customer User", response.getName());
        assertEquals("CUSTOMER", response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should create worker profile when registering a worker")
    void testRegisterWorkerCreatesWorkerProfile() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Worker User");
        request.setEmail("worker@test.com");
        request.setPassword("password123");
        request.setPhone("9876543211");
        request.setRole("WORKER");

        UserDetails workerDetails = org.springframework.security.core.userdetails.User.builder()
                .username("worker@test.com")
                .password("hashedPassword")
                .roles("WORKER")
                .build();

        when(userRepository.existsByEmail("worker@test.com")).thenReturn(false);
        when(userRepository.existsByPhone("9876543211")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleWorkerUser);
        when(userDetailsService.loadUserByUsername("worker@test.com")).thenReturn(workerDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("mock-access-token");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("mock-refresh-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("WORKER", response.getRole());
        verify(workerRepository, times(1)).save(any(Worker.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException if email already registered")
    void testRegisterDuplicateEmailThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("customer@test.com");

        when(userRepository.existsByEmail("customer@test.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully login valid user")
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("customer@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("customer@test.com")).thenReturn(Optional.of(sampleCustomer));
        when(userDetailsService.loadUserByUsername("customer@test.com")).thenReturn(userDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("mock-access-token");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("mock-refresh-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-access-token", response.getAccessToken());
        assertEquals("Customer User", response.getName());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should throw BadCredentialsException on wrong password")
    void testLoginBadCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("customer@test.com");
        request.setPassword("wrongpassword");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
