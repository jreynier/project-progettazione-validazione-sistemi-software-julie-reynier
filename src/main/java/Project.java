import jakarta.persistence.*;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private ResearcherRepository listResearchers;
    @ManyToOne
    @JoinColumn(name = "project_investogator_id")
    private Researcher projectInvestogator;


    public Researcher getProjectInvestogator() {
        return projectInvestogator;
    }

    public void setProjectInvestogator(Researcher projectInvestogator) {
        this.projectInvestogator = projectInvestogator;
    }

}
