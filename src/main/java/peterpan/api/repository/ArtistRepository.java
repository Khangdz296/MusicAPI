package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Artist;
import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // Hàm tìm kiếm nghệ sĩ theo tên (Dùng cho chức năng Search sau này)
    List<Artist> findByNameContainingIgnoreCase(String name);
}