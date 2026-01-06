package peterpan.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Song;
import peterpan.api.model.User;
import peterpan.api.repository.SongRepository;
import peterpan.api.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Thêm bài hát mới (Upload nhạc)
    @PostMapping("/songs")
    public Song addSong(@RequestBody Song song) {
        // Tự động set ngày tạo là ngày hiện tại (nếu cần)
        song.setCreatedAt(java.time.LocalDateTime.now());
        return songRepository.save(song);
    }

    // 2. Xóa bài hát theo ID
    @DeleteMapping("/songs/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Lấy danh sách tất cả User (để quản lý)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 4. Xóa User (Ban người dùng)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
