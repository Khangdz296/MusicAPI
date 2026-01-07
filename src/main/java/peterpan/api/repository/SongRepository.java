package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Song;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // 1. TÌM KIẾM (Search): Giữ nguyên tên hàm cũ để Controller không lỗi
    // Logic: Lấy tham số title so sánh với Title bài hát, tham số artist so sánh với Tên Artist (trong object Artist)
    @Query("SELECT s FROM Song s WHERE " +
            "LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')) OR " +
            "LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :artist, '%'))")
    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(@Param("title") String title, @Param("artist") String artist);

    // 2. TÌM THEO CATEGORY ID (Giữ nguyên tên hàm cũ)
    // Logic: Map từ tham số categoryId vào trường s.category.id
    @Query("SELECT s FROM Song s WHERE s.category.id = :categoryId")
    List<Song> findByCategoryId(@Param("categoryId") Long categoryId);

    // 3. TÌM THEO TÊN NGHỆ SĨ (Giữ nguyên tên hàm cũ)
    // Logic: Map từ tham số string artistName vào trường s.artist.name
    @Query("SELECT s FROM Song s WHERE LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :artistName, '%'))")
    List<Song> findByArtist(@Param("artistName") String artistName);

    // --- CÁC HÀM CŨ (Giữ nguyên) ---

    // A. Lấy 10 bài có Views cao nhất (Cho BXH)
    List<Song> findTop10ByOrderByViewsDesc();

    // B. Lấy 5 bài có ID lớn nhất (Cho mục Mới phát hành)
    List<Song> findTop5ByOrderByCreatedAtDesc();


    // Random
    @Query(value = "SELECT * FROM songs ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Song> findRandomSongs();

    // 7 Ngày (Nếu còn dùng)
    @Query(value = "SELECT * FROM songs WHERE created_at >= NOW() - INTERVAL 7 DAY ORDER BY created_at DESC", nativeQuery = true)
    List<Song> findNewSongs7Days();

    // Tìm bài hát theo ID nghệ sĩ (Dùng cho API lấy chi tiết nghệ sĩ mới)
    List<Song> findByArtist_Id(Long artistId);
}