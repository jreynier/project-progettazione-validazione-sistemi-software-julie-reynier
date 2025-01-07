package it.univr;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany
    private List<Researcher> researchers = new ArrayList<>(); // Researchers working on this project
    @ManyToOne
    private Researcher projectInvestigator;


    public Project() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Researcher> getResearchers() {
        return researchers;
    }

    public void setResearchers(List<Researcher> researchers) {
        this.researchers = researchers;
    }

    public void addResearcher(Researcher researcher) {
        this.researchers.add(researcher);
    }

    public Researcher getProjectInvestigator() {
        return projectInvestigator;
    }

    public void setProjectInvestigator(Researcher projectInvestigator) {
        this.projectInvestigator = projectInvestigator;
    }


}
