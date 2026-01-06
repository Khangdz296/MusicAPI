package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Album;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtist_Id(Long artistId);
    @Query(value = "SELECT a.* FROM albums a " +
            "LEFT JOIN songs s ON a.id = s.album_id " +
            "GROUP BY a.id " +
            "ORDER BY SUM(COALESCE(s.views, 0)) DESC " +
            "LIMIT 50", nativeQuery = true)
    List<Album> findTopAlbumsByTotalViews();
}