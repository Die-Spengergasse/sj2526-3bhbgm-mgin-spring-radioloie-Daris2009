package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "SVNR darf nicht leer sein")
    @Min(value = 1000000000L, message = "SVNR muss 10-stellig sein")
    @Max(value = 9999999999L, message = "SVNR muss 10-stellig sein")
    private Long svnr;

    @NotBlank(message = "Vorname darf nicht leer sein")
    private String vorname;

    @NotBlank(message = "Nachname darf nicht leer sein")
    private String nachname;

    @NotBlank(message = "Geschlecht darf nicht leer sein")
    private String gender;

    @NotNull(message = "Geburtsdatum darf nicht leer sein")
    @Past(message = "Geburtsdatum muss in der Vergangenheit liegen")
    private LocalDate birth;


    public int getId() {
        return id;
    }

    public Long getSvnr() {
        return svnr;
    }

    public void setSvnr(Long svnr) {
        this.svnr = svnr;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }
}