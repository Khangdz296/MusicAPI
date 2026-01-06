package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Song;
import peterpan.api.repository.SongRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    // 1. Lấy toàn bộ bài hát
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // 2. Tìm kiếm bài hát (Theo tiêu đề hoặc nghệ sĩ)
    @GetMapping("/search")
    public List<Song> searchSongs(@RequestParam("q") String keyword) {
        // Gọi hàm repository đã được map @Query chuẩn hóa
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(keyword, keyword);
    }

    // 3. Lấy nhạc mới cập nhật (Top 5 bài ID lớn nhất)
    @GetMapping("/new-updated")
    public List<Song> getNewSongs() {
        return songRepository.findTop5ByOrderByIdDesc();
    }

    // 4. BXH Top Views (Top 10)
    @GetMapping("/top-views")
    public ResponseEntity<List<Song>> getTopSongs() {
        List<Song> topSongs = songRepository.findTop10ByOrderByViewsDesc();
        return ResponseEntity.ok(topSongs);
    }

    // 5. Lấy nhạc ngẫu nhiên (Banner Home)
    @GetMapping("/random")
    public ResponseEntity<List<Song>> getRandomSongs() {
        List<Song> list = songRepository.findRandomSongs();
        return ResponseEntity.ok(list);
    }

    // 6. THÊM MỚI: Tăng lượt xem (Call khi người dùng bắt đầu phát nhạc)
    @PostMapping("/{id}/view")
    public ResponseEntity<String> incrementView(@PathVariable Long id) {
        return songRepository.findById(id).map(song -> {
            song.setViews(song.getViews() + 1);
            songRepository.save(song);
            return ResponseEntity.ok("View count updated: " + song.getViews());
        }).orElse(ResponseEntity.notFound().build());
    }

    // 7. THÊM MỚI: Lấy nhạc của một nghệ sĩ cụ thể (Hỗ trợ màn hình ArtistDetail)
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable Long artistId) {
        List<Song> songs = songRepository.findByArtist_Id(artistId);
        return ResponseEntity.ok(songs);
    }
}