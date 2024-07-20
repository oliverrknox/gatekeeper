package net.gb.knox.gatekeeper.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.gb.knox.gatekeeper.dto.ErrorResponseDTO;
import net.gb.knox.gatekeeper.utility.JWTUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) {
        try {

            final var bearerPrefix = "Bearer ";
            var bearerToken = request.getHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith(bearerPrefix)) {
                filterChain.doFilter(request, response);
                return;
            }

            var token = bearerToken.substring(bearerPrefix.length());

            if (!jwtUtility.verifyToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            var username = jwtUtility.decodeToken(token).getSubject();

            if (username == null) {
                filterChain.doFilter(request, response);
                return;
            }

            var userDetails = userDetailsService.loadUserByUsername(username);
            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
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
            } catch (IOException ignored) {
            }
        }
    }
}
