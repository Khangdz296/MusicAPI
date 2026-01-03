package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Song;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist);

    @Query(value = "SELECT * FROM songs WHERE created_at >= NOW() - INTERVAL 7 DAY ORDER BY created_at DESC", nativeQuery = true)
    List<Song> findNewSongs7Days();

    List<Song> findByCategoryId(Long categoryId);
    List<Song> findByArtist(String artistName);
}