package cl.ejercicio.java.security.jwt;

import cl.ejercicio.java.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private Key secretKey;

    /**
     * Inicializa la clave secreta para firmar y verificar los JWT.
     */
    @PostConstruct
    public void init() {
        String secret = jwtProperties.getSecret();
        String algorithm = jwtProperties.getAlgorithm();

        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("La clave secreta debe tener al menos 32 caracteres.");
        }

        SignatureAlgorithm.valueOf(algorithm); // Verifica que sea válido

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un JWT firmado con el correo electrónico del usuario y sus roles.
     *
     * @param email Email del usuario autenticado
     * @param roles Lista de roles del usuario
     * @return JWT firmado
     */
    public String generateToken(String email, Collection<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMillis());

        return Jwts.builder()
                .setSubject(email)
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.valueOf(jwtProperties.getAlgorithm()))
                .compact();
    }

    /**
     * Extrae el email (subject) desde un JWT válido.
     *
     * @param token JWT
     * @return Email contenido como subject
     * @throws JwtException si el token es inválido o expirado
     */
    public String getUsernameFromToken(String token) {
        log.info("Extrayendo username del token: {}", token);
        return validateAndParseToken(token).getSubject();
    }

    /**
     * Extrae los roles desde un JWT válido.
     *
     * @param token JWT
     * @return Lista de roles
     * @throws JwtException si el token es inválido o expirado
     */
    public List<String> getRolesFromToken(String token) {
        log.info("Extrayendo roles del token: {}", token);
        Object rolesObj = validateAndParseToken(token).get("roles");

        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }

        return Collections.emptyList();
    }

    /**
     * Valida y parsea los claims de un JWT.
     *
     * @param token JWT
     * @return Claims del token
     * @throws JwtException si el token es inválido o expirado
     */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Valida completamente un token JWT: firma, expiración, issuer y audience.
     *
     * @param token Token JWT
     * @return Claims válidos si pasa todas las validaciones
     * @throws JwtException si el token es inválido
     */
    public Claims validateAndParseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (!jwtProperties.getIssuer().equals(claims.getIssuer())) {
            throw new JwtException("Issuer inválido");
        }

        if (!jwtProperties.getAudience().equals(claims.getAudience())) {
            throw new JwtException("Audience inválido");
        }

        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("Token expirado");
        }

        return claims;
    }

    /**
     * Valida si el token JWT es válido para el usuario dado.
     *
     * @param token JWT recibido en la solicitud
     * @param userDetails datos del usuario autenticado
     * @return true si el token es válido y pertenece al usuario, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return getUsernameFromToken(token).equals(userDetails.getUsername())
                && !validateAndParseToken(token).getExpiration().before(new Date());
    }
    /**
     * Verifica si un JWT ha expirado.
     *
     * @param token JWT
     * @return true si expiró, false si es válido
     */
    public boolean isTokenExpired(String token) {
        return validateAndParseToken(token).getExpiration().before(new Date());
    }
}
