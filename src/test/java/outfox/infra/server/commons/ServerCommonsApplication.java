package outfox.infra.server.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ServerCommonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerCommonsApplication.class, args);
    }

}
