package it.univr;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface HoursRepository extends CrudRepository<Hours, Long> {
    List<Hours> findByResearcher(Researcher researcher);
    List<Hours> findByProject(Project project);
    Hours findById(long id);

}
