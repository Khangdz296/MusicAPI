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
    // A. Lấy 10 bài có Views cao nhất (Cho BXH)
    List<Song> findTop10ByOrderByViewsDesc();

    // B. Lấy 5 bài có ID lớn nhất (Cho mục Mới phát hành - thay cho cái 7 ngày phức tạp)
    List<Song> findTop5ByOrderByIdDesc();

    @Query(value = "SELECT * FROM songs ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Song> findRandomSongs();
}