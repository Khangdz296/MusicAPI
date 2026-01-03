package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Artist;
import peterpan.api.model.Song;
import peterpan.api.repository.ArtistRepository;
import peterpan.api.repository.SongRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    // API 1: Lấy danh sách tất cả nghệ sĩ
    @GetMapping
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // API 2: Lấy danh sách bài hát của một nghệ sĩ cụ thể (theo ID nghệ sĩ)
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongsByArtistId(@PathVariable Long id) {
        Optional<Artist> artistOptional = artistRepository.findById(id);

        if (artistOptional.isPresent()) {
            Artist artist = artistOptional.get();

            String artistName = artist.getName();

            List<Song> songs = songRepository.findByArtist(artistName);

            return ResponseEntity.ok(songs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}