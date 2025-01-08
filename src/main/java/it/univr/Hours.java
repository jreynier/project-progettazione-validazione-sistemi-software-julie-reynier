package it.univr;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Hours {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int hourWorked;
    private LocalDate date;
    @ManyToOne
    private Researcher researcher;
    @ManyToOne
    private Project project;

    private boolean approved;

    public Hours() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHourWorked() {
        return hourWorked;
    }

    public void setHourWorked(int hoursWorked) {
        this.hourWorked = hoursWorked;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
