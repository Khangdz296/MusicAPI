package peterpan.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Artist;
import peterpan.api.model.Category;
import peterpan.api.model.Song;
import peterpan.api.model.User;
import peterpan.api.repository.ArtistRepository;
import peterpan.api.repository.CategoryRepository;
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

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    // Trong AdminController.java
    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return songRepository.findById(id).map(song -> {
            song.setTitle(songDetails.getTitle());
            song.setArtist(songDetails.getArtist());
            song.setImageUrl(songDetails.getImageUrl());
            song.setFileUrl(songDetails.getFileUrl());
            song.setDuration(songDetails.getDuration());
            song.setCategory(songDetails.getCategory());
            // ... cập nhật các trường khác ...
            return ResponseEntity.ok(songRepository.save(song));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/artists")
    public Artist addArtist(@RequestBody Artist artist) {
        return artistRepository.save(artist);
    }

    @PutMapping("/artists/{id}")
    public Artist updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        // Logic tìm theo ID và update set name, image...
        return artistRepository.save(artist);
    }

    @DeleteMapping("/artists/{id}")
    public void deleteArtist(@PathVariable Long id) {
        artistRepository.deleteById(id);
    }

    // URL: POST /api/admin/categories
    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category category) {
        // Có thể thêm logic kiểm tra dữ liệu rỗng ở đây nếu muốn
        return categoryRepository.save(category);
    }

    // 3. CẬP NHẬT CATEGORY (SỬA)
    // URL: PUT /api/admin/categories/{id}
    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Cập nhật thông tin mới
        category.setName(categoryDetails.getName());
        category.setImageUrl(categoryDetails.getImageUrl());

        // Lưu xuống DB
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    // 4. XÓA CATEGORY
    // URL: DELETE /api/admin/categories/{id}
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build(); // Trả về 200 OK
        }
        return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy ID
    }
}
