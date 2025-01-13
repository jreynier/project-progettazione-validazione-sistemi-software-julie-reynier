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
    private String fundingAgency;
    private String projectCode;
    @ManyToMany
    private List<Researcher> researchers = new ArrayList<>(); // Researchers working on this project
    @ManyToOne
    private Researcher projectInvestigator;
    @OneToMany(mappedBy = "project")
    private List<Hours> hours = new ArrayList<>();



    public Project() {

    }

    public Project(String name, String fundingAgency, String projectCode) {
        this.name = name;
        this.fundingAgency = fundingAgency;
        this.projectCode = projectCode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFundingAgency() {
        return fundingAgency;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public List<Researcher> getResearchers() {
        return researchers;
    }

    public void addResearcher(Researcher researcher) {
        this.researchers.add(researcher);
    }

    public Researcher getProjectInvestigator() {
        return projectInvestigator;
    }

    public void setProjectInvestigator(Researcher projectInvestigator) {
        this.projectInvestigator = projectInvestigator;
        addResearcher(projectInvestigator);     // the PI works on the project
    }

    public List<Hours> getHours() {
        return hours;
    }

    public void addHours(Hours hours) {
        this.hours.add(hours);
    }
}
