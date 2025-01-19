package by.youngliqui.authservice.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private static final String USERNAME = "test_name";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtService = new JwtService();
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, "G4FEVk7763crEaNKV0GynmfugR3Gc1c6DumD15Sfi1AqX3WLRz0uGpEK28ZtDnhc");

        Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, 3000L);

        secretKeyField.setAccessible(false);
        expirationField.setAccessible(false);

        when(userDetails.getUsername()).thenReturn(USERNAME);
    }

    @Test
    void testGenerateToken() {
        String actualResult = jwtService.generateToken(userDetails);

        // Then
        assertThat(actualResult).isNotBlank();
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        // Then
        assertThat(username)
                .isNotBlank()
                .isEqualTo(USERNAME);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void testIsTokenValid_InvalidToken() {
        String token = jwtService.generateToken(userDetails);
        String invalidToken = token + "_INVALID";

        assertThatThrownBy(() ->
                jwtService.isTokenValid(invalidToken, userDetails))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void testIsTokenValid_InvalidUsername() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        when(userDetails.getUsername()).thenReturn("different_user");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void testIsTokenExpired() throws InterruptedException {
        String token = jwtService.generateToken(userDetails);
        Thread.sleep(5000);

        assertThatThrownBy(() ->
                jwtService.isTokenValid(token, userDetails))
                .isInstanceOf(ExpiredJwtException.class);
    }
}