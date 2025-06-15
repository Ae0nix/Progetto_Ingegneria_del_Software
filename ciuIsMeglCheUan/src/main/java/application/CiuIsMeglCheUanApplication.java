package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"application", "control", "boundary"})
public class CiuIsMeglCheUanApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiuIsMeglCheUanApplication.class, args);
    }

}
