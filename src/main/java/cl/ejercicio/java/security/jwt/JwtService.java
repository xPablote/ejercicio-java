package cl.ejercicio.java.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${client.secret}")
    private String secret;

    private static final long EXPIRATION_TIME_MILLIS = 30 * 60 * 1000; // 30 minutos

    private Key secretKey;

    /**
     * Método para generar un JWT firmado.
     *
     * @param email del usuario que se autentica.
     * @return un JWT firmado.
     */
    public String generateToken(String email) {
        Key secretKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        long expirationTimeMillis = 30 * 60 * 1000; // 30 minutos = 30 x 60 x 1000 ms

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(SignatureAlgorithm.HS256,  secretKey)
                .compact();
    }

    /**
     * Método para validar el JWT.
     *
     * @param token el JWT a validar.
     * @return los claims del JWT si es válido.
     * @throws Exception si el token no es válido o ha expirado.
     */
    public Claims validateToken(String token) throws Exception {
        Key secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        String username = extractUsername(token);


        return Jwts.parser()  // Aquí sí debe funcionar ahora
                .setSigningKey(secretKey) // Clave para verificar firma
                .build()
                .parseClaimsJws(token) // Parsear el JWT
                .getBody(); // Devolver solo el body (Claims)
    }

    /**
     * Método para obtener el email del JWT (usado después de la validación).
     *
     * @param token el JWT.
     * @return el email extraído del token.
     */
    public String getEmailFromToken(String token) throws Exception {
        Claims claims = validateToken(token);
        return claims.getSubject(); // El "subject" es el email
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
