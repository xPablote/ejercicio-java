package cl.ejercicio.java.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuración personalizada para el pool de conexiones HikariCP.
 */
@Configuration
public class DataSourceConfig {

    /**
     * Configura manualmente el pool de conexiones HikariCP.
     *
     * @return instancia personalizada de {@link DataSource}
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");

        // Configuraciones de pool
        config.setPoolName("MiHikariCP");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);         // 30 segundos
        config.setConnectionTimeout(30000);    // 30 segundos
        config.setLeakDetectionThreshold(20000); // 20 segundos

        return new HikariDataSource(config);
    }
}
