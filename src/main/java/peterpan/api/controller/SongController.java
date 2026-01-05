package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Song;
import peterpan.api.repository.SongRepository;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    // API 1: Lấy toàn bộ bài hát (Có thể dùng cho danh sách gợi ý)
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // API 2: Tìm kiếm bài hát
    @GetMapping("/search")
    public List<Song> searchSongs(@RequestParam("q") String keyword) {
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(keyword, keyword);
    }


//    // API 3: Lấy nhạc mới cập nhật 7 ngày
//    @GetMapping("/new-updated")
//    public List<Song> getNewSongs() {
//        return songRepository.findNewSongs7Days();
//    }

    @GetMapping("/new-updated")
    public List<Song> getNewSongs() {
        return songRepository.findTop5ByOrderByIdDesc();
    }

    // THÊM MỚI: API 4 - Lấy BXH Top Views
    @GetMapping("/top-views")
    public ResponseEntity<List<Song>> getTopSongs() {
        List<Song> topSongs = songRepository.findTop10ByOrderByViewsDesc();
        return ResponseEntity.ok(topSongs);
    }

}