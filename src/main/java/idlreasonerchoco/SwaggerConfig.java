package idlreasonerchoco;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	  @Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info()
	              .title("IDLReasoner API")
	              .contact(new Contact().name("Saman A. Barakat").email("saman.barakat@gmail.com"))
	              .description("IDLReasonerAPI is a web-based API that complements the IDLReasoner library, offering a set of analysis operations specifically tailored for IDL (Inter-parameter Dependency Language) within the OpenAPI Specification (OAS). The API allows users to assess the consistency of IDL in OAS documents. It verifies the validity of OAS files, examines user requests, and provides detailed error explanations when inconsistencies or issues are detected. With IDLReasonerAPI, developers can easily leverage the power of IDLReasoner for comprehensive analysis and validation of their OAS documents enriched with IDL.")
	              .version("0.0.1")
	              .license(new License().name("GPL-3.0")))
	              .externalDocs(new ExternalDocumentation()
	              .description("IDReasoner API Documentation")
	              .url("http://localhost:8085/api-docs")
				  );
	  }
}
