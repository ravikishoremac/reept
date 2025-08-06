
package reept.reept;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder; 
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer; 
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "reept.reept")
@EnableScheduling
public class ReeptApplication extends SpringBootServletInitializer {  

    public static void main(String[] args) {
        SpringApplication.run(ReeptApplication.class, args);
    }

   
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ReeptApplication.class);
    }
}
