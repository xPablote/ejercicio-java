package cl.ejercicio.java;

import cl.ejercicio.java.config.RegexProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RegexProperties.class)
@SpringBootApplication
public class EjercicioJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjercicioJavaApplication.class, args);
	}

}
