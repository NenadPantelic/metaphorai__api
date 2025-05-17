package ai.metaphor.api;

import ai.metaphor.api.properties.AuthConfigProperties;
import ai.metaphor.api.properties.UserDetailsConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "ai.metaphor.api", basePackageClasses = {
		AuthConfigProperties.class,
		UserDetailsConfigProperties.class
})
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
