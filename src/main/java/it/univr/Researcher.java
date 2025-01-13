package it.univr;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Researcher {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String fiscalCode;
    @ManyToMany(mappedBy = "researchers")
    private List<Project> projects = new ArrayList<>();
    @OneToMany(mappedBy = "projectInvestigator")
    private List<Project> projectsAsPI = new ArrayList<>();
    @OneToMany(mappedBy = "researcher")
    private List<Hours> hours = new ArrayList<>();
    private final List<LocalDate> daysOff = new ArrayList<>();

    public Researcher() {

    }

    public Researcher(String firstName, String lastName, String email, String password, String fiscalCode) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.fiscalCode = fiscalCode;
    }
    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public List<Project> getProjectsAsPI() {
        return projectsAsPI;
    }

    public void addProjectsAsPI(Project projectAsPI) {
        projectsAsPI.add(projectAsPI);
        projects.add(projectAsPI); // the pi works on the project
    }

    public List<Hours> getHours() {
        return hours;
    }

    public void addHours(Hours hours) {
        this.hours.add(hours);
    }

    public boolean isDayOff(LocalDate date) {
        return daysOff.contains(date);
    }

    public List<LocalDate> getDaysOff() {
        return daysOff;
    }

    public void addDaysOff(LocalDate daysOff) {
        this.daysOff.add(daysOff);
    }
}
