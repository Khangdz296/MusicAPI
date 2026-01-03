package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Playlist;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    // Tìm các playlist công khai (Mục 2.7)
    List<Playlist> findByIsPublicTrue();

    // Tìm playlist của một user cụ thể (Mục 2.5 - để hiện BottomSheet)
    List<Playlist> findByUserId(Long userId);
}