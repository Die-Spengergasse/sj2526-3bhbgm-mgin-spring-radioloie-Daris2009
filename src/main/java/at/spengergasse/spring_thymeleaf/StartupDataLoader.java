package at.spengergasse.spring_thymeleaf;

import at.spengergasse.spring_thymeleaf.entities.BodyRegion;
import at.spengergasse.spring_thymeleaf.entities.BodyRegionRepository;
import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class StartupDataLoader implements CommandLineRunner {

    private final DeviceRepository deviceRepository;
    private final BodyRegionRepository bodyRegionRepository;

    public StartupDataLoader(DeviceRepository deviceRepository, BodyRegionRepository bodyRegionRepository) {
        this.deviceRepository = deviceRepository;
        this.bodyRegionRepository = bodyRegionRepository;
    }

    @Override
    public void run(String... args) {
        // Devices: MRT, CT, Ultraschall, Röntgen
        addDeviceIfMissing("MRT 1", "MR", "Raum 101");
        addDeviceIfMissing("CT 1", "CT", "Raum 102");
        addDeviceIfMissing("Ultraschall 1", "Ultraschall", "Raum 103");
        addDeviceIfMissing("Röntgen 1", "Röntgen", "Raum 104");

        // Body regions: expanded list
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("HEAD", "Kopf / Hals");
        regions.put("NECK", "Hals");
        regions.put("CHEST", "Thorax / Brustkorb");
        regions.put("HEART", "Herz / Kardiologie");
        regions.put("ABDOMEN", "Abdomen / Bauch");
        regions.put("PELVIS", "Becken");
        regions.put("UPPER_EXTREMITY", "Obere Extremität");
        regions.put("LOWER_EXTREMITY", "Untere Extremität");
        regions.put("SPINE_CERVICAL", "Wirbelsäule - HWS");
        regions.put("SPINE_THORACIC", "Wirbelsäule - BWS");
        regions.put("SPINE_LUMBAR", "Wirbelsäule - LWS");
        regions.put("SOFT_TISSUE", "Weichteile");

        for (Map.Entry<String, String> e : regions.entrySet()) {
            addRegionIfMissing(e.getKey(), e.getValue());
        }
    }

    private void addDeviceIfMissing(String bezeichnung, String typ, String standort) {
        boolean exists = deviceRepository.findAll().stream()
                .anyMatch(d -> bezeichnung.equalsIgnoreCase(d.getBezeichnung()));
        if (!exists) {
            Device d = new Device();
            d.setBezeichnung(bezeichnung);
            d.setGeraetetyp(typ);
            d.setStandort(standort);
            deviceRepository.save(d);
        }
    }

    private void addRegionIfMissing(String code, String description) {
        boolean exists = bodyRegionRepository.findAll().stream()
                .anyMatch(r -> code.equalsIgnoreCase(r.getCode()));
        if (!exists) {
            BodyRegion br = new BodyRegion();
            br.setCode(code);
            br.setDescription(description);
            bodyRegionRepository.save(br);
        }
    }
}
