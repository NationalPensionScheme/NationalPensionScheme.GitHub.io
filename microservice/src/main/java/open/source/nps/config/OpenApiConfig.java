package open.source.nps.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		title = "Turquoise Space - National Pension Scheme - APIs",
		version = "v1",
		description = "This documentation app provides REST APIs for Turquoise Space - National Pension Scheme",
		contact = @Contact(
			email = "national-pension-scheme@googlegroups.com"
		)
	),
	servers = {
		@Server(
			url = "http://localhost:7777",
			description = "National-Pension-Scheme"
		)
	}
)
public class OpenApiConfig {

	// Type in the URL in any browser

	// http://localhost:7777/webjars/swagger-ui/index.html?url=http://localhost:7777/v3/api-docs

	// http://127.0.0.1:7777/webjars/swagger-ui/index.html?url=http://127.0.0.1:7777/v3/api-docs

	// http://localhost:7777/swagger-ui.html#/

	// http://127.0.0.1:7777/swagger-ui.html#/

	// http://localhost:7777/webjars/swagger-ui/index.html?url=/v3/api-docs&validatorUrl=#/

}
