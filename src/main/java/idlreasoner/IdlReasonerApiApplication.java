package idlreasoner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class IdlReasonerApiApplication {

	public static void main(String[] args) {
		// Set proxy properties
		System.setProperty("http.proxyHost", "proxy.int.local");
		System.setProperty("http.proxyPort", "3128");
		System.setProperty("https.proxyHost", "proxy.int.local");
		System.setProperty("https.proxyPort", "3128");
		SpringApplication.run(IdlReasonerApiApplication.class, args);
	}

}
