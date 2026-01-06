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

    // API 1: Lấy tất cả danh mục
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // API 2: Lấy bài hát trong một danh mục
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongsByCategory(@PathVariable Long id) {
        // 1. Kiểm tra danh mục có tồn tại không
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // 2. Gọi hàm tìm kiếm mới trong Repository
        // Cũ: findByCategoryId(id) -> Sai quy tắc đặt tên mới
        // Mới: findByCategory_Id(id) -> JPA hiểu là tìm trong object Category lấy cái Id
        List<Song> songs = songRepository.findByCategoryId(id);

        return ResponseEntity.ok(songs);
    }
}