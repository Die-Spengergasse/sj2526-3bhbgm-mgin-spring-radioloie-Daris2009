package at.spengergasse.spring_thymeleaf;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringThymeleafApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(DeviceRepository deviceRepository, BodyRegionRepository bodyRegionRepository) {
        return args -> {
            if (deviceRepository.count() == 0) {
                Device d1 = new Device();
                d1.setBezeichnung("MR-01");
                d1.setGeraetetyp("MR");
                d1.setStandort("R101");
                deviceRepository.save(d1);

                Device d2 = new Device();
                d2.setBezeichnung("CT-01");
                d2.setGeraetetyp("CT");
                d2.setStandort("R102");
                deviceRepository.save(d2);

                Device d3 = new Device();
                d3.setBezeichnung("RO-01");
                d3.setGeraetetyp("Röntgen");
                d3.setStandort("R103");
                deviceRepository.save(d3);
            }

            if (bodyRegionRepository.count() == 0) {
                BodyRegion br1 = new BodyRegion();
                br1.setCode("KOPF");
                br1.setDescription("Kopf");
                bodyRegionRepository.save(br1);

                BodyRegion br2 = new BodyRegion();
                br2.setCode("BRUST");
                br2.setDescription("Brustkorb");
                bodyRegionRepository.save(br2);

                BodyRegion br3 = new BodyRegion();
                br3.setCode("BEIN");
                br3.setDescription("Beine");
                bodyRegionRepository.save(br3);
            }
        };
    }
}
