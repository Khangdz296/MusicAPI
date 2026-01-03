package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}