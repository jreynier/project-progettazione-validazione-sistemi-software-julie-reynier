package it.univr;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ResearcherRepository repository, ProjectRepository projectRepository) {
        return args -> {
            // Check if a researcher already exists to avoid duplicates
            if (repository.findByEmail("test@example.com") == null) {
                // Create a mock researcher
                Researcher mockResearcher = new Researcher();
                mockResearcher.setEmail("a@a.c");
                mockResearcher.setPassword("password123");
                mockResearcher.setFirstName("Name");
                mockResearcher.setLastName("LastName");
                Project project = new Project();
                project.setName("Le projet 1");
                project.setProjectInvestigator(mockResearcher);
                mockResearcher.getProjectsAsPI().add(project);
                System.out.println("!!!!>"+mockResearcher.getProjectsAsPI());
                Project project2 = new Project();
                project2.setName("Le projet 2");
                project2.addResearcher(mockResearcher);
                mockResearcher.addProject(project2);
                repository.save(mockResearcher);
                projectRepository.save(project);
                projectRepository.save(project2);
                System.out.println("!!!!>"+mockResearcher.getProjectsAsPI());
            }
        };
    }
}
