package it.univr;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ResearcherRepository extends CrudRepository<Researcher, Long> {

    Researcher findByEmail(String email);

    Researcher findById(long id);
}