package it.univr;

import jakarta.persistence.*;

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
    @ManyToMany(mappedBy = "researchers")
    private List<Project> projects = new ArrayList<>();
    @OneToMany(mappedBy = "projectInvestigator")
    private List<Project> projectsAsPI = new ArrayList<>();
    @OneToMany(mappedBy = "researcher")
    private List<Hours> hours = new ArrayList<>();

    public Researcher(Long id, String lastName, String firstName, String email, String password) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public Researcher() {

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
    }

    public List<Hours> getHours() {
        return hours;
    }

    public void addHours(Hours hours) {
        this.hours.add(hours);
    }
}