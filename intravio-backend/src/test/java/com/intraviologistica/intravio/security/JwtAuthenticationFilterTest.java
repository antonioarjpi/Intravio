package com.intraviologistica.intravio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Test
    void doFilterInternal_TokenAndUsuarioValido_AutenticaUsuario() throws ServletException, IOException {
        String token = "valid-token";
        String userEmail = "user@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(userEmail);
        verify(jwtService).isTokenValid(token, userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_TokenInvalido_NaoAutenticaUsuario() throws ServletException, IOException {
        String token = "token-invalido";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verifyNoInteractions(userDetailsService);
        verifyNoMoreInteractions(jwtService);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_DadosDeUsuarioNaoEncontrado_NaoAutenticaUsuario() throws ServletException, IOException {
        String token = "token_valido";
        String userEmail = "emailnaoencontrado@email.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenThrow(new UsernameNotFoundException(userEmail));

        assertThrows(UsernameNotFoundException.class, () ->
                jwtAuthenticationFilter.doFilterInternal(request, response, filterChain)
        );

        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(userEmail);
        verifyNoMoreInteractions(jwtService);
        verifyNoMoreInteractions(filterChain);
    }

    @Test
    void doFilterInternal_formatoDoTokenInvalido_NaoAutenticaUsuario() throws ServletException, IOException {
        String token = "token-invalido";

        when(request.getHeader("Authorization")).thenReturn(token);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
        verify(filterChain).doFilter(request, response);
    }
}