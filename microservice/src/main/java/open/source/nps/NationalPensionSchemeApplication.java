package open.source.nps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "open.source.nps" })
public class NationalPensionSchemeApplication {

	public static void main(String[] args) {

		SpringApplication.run(NationalPensionSchemeApplication.class, args);
	}

}
