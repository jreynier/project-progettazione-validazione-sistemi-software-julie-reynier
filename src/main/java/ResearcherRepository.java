import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ResearcherRepository extends CrudRepository<Researcher, Long> {

    List<Researcher> findByLastName(String lastName);

    Researcher findById(long id);
}