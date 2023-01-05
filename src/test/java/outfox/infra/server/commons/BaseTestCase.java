package outfox.infra.server.commons;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;


//@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerCommonsApplication.class)
@ActiveProfiles("test")
public class BaseTestCase {

    @Configuration
    @ComponentScan("outfox.infra.server.commons")
    public static class SpringConfig {

        @Bean
        public HttpServletRequest httpServletRequest() {
            return Mockito.mock(HttpServletRequest.class);
        }
    }
}
