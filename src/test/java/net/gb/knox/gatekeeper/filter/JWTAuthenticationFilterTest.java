package net.gb.knox.gatekeeper.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.gb.knox.gatekeeper.utility.JWTUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JWTAuthenticationFilterTest {

    private static final HttpServletRequest HTTP_SERVLET_REQUEST_MOCK = mock(HttpServletRequest.class);
    private static final HttpServletResponse HTTP_SERVLET_RESPONSE_MOCK = mock(HttpServletResponse.class);
    private static final FilterChain FILTER_CHAIN_MOCK = mock(FilterChain.class);
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JWTUtility jwtUtility;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void testDoFilterInternal() {
        var token = jwtUtility.createToken("TestUser");
        when(HTTP_SERVLET_REQUEST_MOCK.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(
                HTTP_SERVLET_REQUEST_MOCK,
                HTTP_SERVLET_RESPONSE_MOCK,
                FILTER_CHAIN_MOCK
        );

        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    }

    @Test
    public void testDoFilterInternalInvalidBearer() {
        when(HTTP_SERVLET_REQUEST_MOCK.getHeader("Authorization")).thenReturn("");

        jwtAuthenticationFilter.doFilterInternal(
                HTTP_SERVLET_REQUEST_MOCK,
                HTTP_SERVLET_RESPONSE_MOCK,
                FILTER_CHAIN_MOCK
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternalInvalidToken() {
        when(HTTP_SERVLET_REQUEST_MOCK.getHeader("Authorization")).thenReturn("Bearer x");

        jwtAuthenticationFilter.doFilterInternal(
                HTTP_SERVLET_REQUEST_MOCK,
                HTTP_SERVLET_RESPONSE_MOCK,
                FILTER_CHAIN_MOCK
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternalNoSubject() {
        var token = jwtUtility.createToken(null);
        when(HTTP_SERVLET_REQUEST_MOCK.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(
                HTTP_SERVLET_REQUEST_MOCK,
                HTTP_SERVLET_RESPONSE_MOCK,
                FILTER_CHAIN_MOCK
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternalException() throws IOException {
        var token = jwtUtility.createToken("TestUser");
        var printer = mock(PrintWriter.class);

        when(HTTP_SERVLET_REQUEST_MOCK.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(HTTP_SERVLET_RESPONSE_MOCK.getWriter()).thenReturn(printer);
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("Missing user."));

        jwtAuthenticationFilter.doFilterInternal(
                HTTP_SERVLET_REQUEST_MOCK,
                HTTP_SERVLET_RESPONSE_MOCK,
                FILTER_CHAIN_MOCK
        );

        verify(HTTP_SERVLET_RESPONSE_MOCK).setStatus(500);
        verify(HTTP_SERVLET_RESPONSE_MOCK).setContentType("application/json");
        verify(HTTP_SERVLET_RESPONSE_MOCK).setCharacterEncoding("UTF-8");
        verify(printer).write(anyString());
        verify(printer).flush();
    }
}
