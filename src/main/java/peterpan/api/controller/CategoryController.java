package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Category;
import peterpan.api.model.Song;
import peterpan.api.repository.CategoryRepository;
import peterpan.api.repository.SongRepository;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SongRepository songRepository;

    // API 1: Lấy tất cả danh mục (Cho màn hình Search Explore)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // API 2: Lấy bài hát trong một danh mục (Khi bấm vào ô màu)
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongsByCategory(@PathVariable Long id) {
        // Kiểm tra xem category có tồn tại không
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Song> songs = songRepository.findByCategoryId(id);
        return ResponseEntity.ok(songs);
    }
}