package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Album;
import peterpan.api.repository.AlbumRepository;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    // API 1: Lấy tất cả Album (Cho màn hình Library/Home)
    @GetMapping
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    // API 2: Lấy chi tiết 1 Album theo ID (Cho màn hình AlbumDetailActivity)
    // Khi gọi API này, nhờ cấu hình trong Model, nó sẽ trả về cả list bài hát bên trong.
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}