package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Album;
import peterpan.api.model.Artist;
import peterpan.api.model.Song;
import peterpan.api.repository.AlbumRepository;
import peterpan.api.repository.ArtistRepository;
import peterpan.api.repository.SongRepository;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // API 1: Lấy danh sách tất cả nghệ sĩ
    @GetMapping
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // API 2: Lấy danh sách bài hát của một nghệ sĩ cụ thể (theo ID nghệ sĩ)
    // Đường dẫn: /api/artists/{id}/songs
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongsByArtistId(@PathVariable Long id) {
        if (!artistRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Song> songs = songRepository.findByArtist_Id(id);
        return ResponseEntity.ok(songs);
    }

    // API 3: Lấy danh sách Album của một nghệ sĩ cụ thể (theo ID nghệ sĩ)
    // Đường dẫn: /api/artists/{id}/albums
    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getAlbumsByArtistId(@PathVariable Long id) {
        if (!artistRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Logic tìm Album theo ID nghệ sĩ
        List<Album> albums = albumRepository.findByArtist_Id(id);
        return ResponseEntity.ok(albums);
    }
}