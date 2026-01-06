package peterpan.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peterpan.api.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Hiện tại chưa cần hàm query phức tạp, dùng mặc định là đủ
}