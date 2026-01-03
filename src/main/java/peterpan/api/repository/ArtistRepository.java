package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}