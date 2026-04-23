package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bezeichnung;
    private String geraetetyp;
    private String standort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getGeraetetyp() {
        return geraetetyp;
    }

    public void setGeraetetyp(String geraetetyp) {
        this.geraetetyp = geraetetyp;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }
}
