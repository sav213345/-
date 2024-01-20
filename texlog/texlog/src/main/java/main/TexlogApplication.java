package main;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ComponentScan(basePackages = {"main"})
public class TexlogApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
             SpringApplication.run(TexlogApplication.class, args);
            //new TexlogApplication().configure(new SpringApplicationBuilder(TexlogApplication.class)).run(args);
  
	}
}
