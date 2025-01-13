package it.univr;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Hours {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int hoursWorked;
    private LocalDate date;
    private boolean approved;

    @ManyToOne
    private Researcher researcher;
    @ManyToOne
    private Project project;
    
    public Hours() {

    }

    public Hours(int hoursWorked, LocalDate date, boolean approved, Researcher researcher, Project project) {
        this.hoursWorked = hoursWorked;
        this.date = date;
        this.approved = approved;
        this.researcher = researcher;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
