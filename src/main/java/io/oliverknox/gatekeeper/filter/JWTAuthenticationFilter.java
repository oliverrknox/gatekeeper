package io.oliverknox.gatekeeper.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.oliverknox.gatekeeper.dto.ErrorResponseDTO;
import io.oliverknox.gatekeeper.utility.JWTUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        logger.info("ENTER doFilterInternal");
        try {
            final var bearerPrefix = "Bearer ";
            var bearerToken = request.getHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith(bearerPrefix)) {
                logger.info("EXIT EARLY doFilterInternal | reason=Bearer token is invalid.");
                filterChain.doFilter(request, response);
                return;
            }

            var token = bearerToken.substring(bearerPrefix.length());

            if (!jwtUtility.verifyToken(token)) {
                logger.info("EXIT EARLY doFilterInternal | reason=Unable to verify token.");
                filterChain.doFilter(request, response);
                return;
            }

            var username = jwtUtility.decodeToken(token).getSubject();

            if (username == null) {
                logger.info("EXIT EARLY doFilterInternal | reason=No username in token.");
                filterChain.doFilter(request, response);
                return;
            }

            var userDetails = userDetailsService.loadUserByUsername(username);
            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            logger.info("EXIT doFilterInternal");
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            logger.info("Exception doFilterInternal | message={}", ex.getMessage());

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                var errorResponseDTO = new ErrorResponseDTO("Something went wrong.");
                var json = objectMapper.writeValueAsString(errorResponseDTO);

                try (var writer = response.getWriter()) {
                    writer.write(json);
                    writer.flush();
                }
            } catch (IOException ioEx) {
                logger.info("IOException doFilterInternal | message={}", ioEx.getMessage());
            }
        }
    }
}
