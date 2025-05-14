package cl.ejercicio.java.security.jwt;

import cl.ejercicio.java.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro que valida el token JWT en cada petición.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        log.info("JwtAuthenticationFilter inicializado correctamente");
    }

    /**
     * Define si este filtro debe ignorar la petición.
     *
     * @param request la petición HTTP
     * @return true si no debe aplicar el filtro
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Solo excluye endpoints públicos específicos
        return path.startsWith("/api/v1/auth") ||
                (path.startsWith("/api/v1/users") &&
                        request.getMethod().equals("GET"));
    }

    /**
     * Ejecuta la validación del JWT.
     *
     * @param request     la petición HTTP
     * @param response    la respuesta HTTP
     * @param filterChain la cadena de filtros
     * @throws ServletException en caso de error de filtro
     * @throws IOException      en caso de error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Procesando solicitud en JwtAuthenticationFilter: {}", request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("Encabezado Authorization encontrado: {}", authHeader);
            String jwt = authHeader.substring(7);
            String username = jwtService.getUsernameFromToken(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Usuario encontrado en token: {}", username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    log.info("Token válido, configurando contexto de seguridad");
                    setAuthenticationContext(request, userDetails, jwt);
                } else {
                    log.warn("Token inválido para usuario: {}", username);
                }
            } else {
                log.warn("No se encontró usuario o ya hay autenticación: {}", username);
            }
        } else {
            log.warn("No se encontró encabezado Authorization o no comienza con Bearer");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado Authorization.
     *
     * @param authHeader el encabezado Authorization
     * @return el token JWT
     */

    /**
     * Establece el contexto de autenticación para el usuario.
     *
     * @param request     la petición HTTP
     * @param userDetails los detalles del usuario
     * @param jwt         el token JWT
     */
    private void setAuthenticationContext(HttpServletRequest request, UserDetails userDetails, String jwt) {
        List<String> roles = jwtService.getRolesFromToken(jwt);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        log.info("Autoridades asignadas desde el token: {}", authorities.stream().map(GrantedAuthority::getAuthority).toList());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
