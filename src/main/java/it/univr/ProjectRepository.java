package it.univr;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    Project findById(long id);
}
