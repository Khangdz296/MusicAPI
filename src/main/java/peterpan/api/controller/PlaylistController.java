package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Playlist;
import peterpan.api.model.Song;
import peterpan.api.repository.PlaylistRepository;
import peterpan.api.repository.SongRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository; // Cần cái này để tìm bài hát khi thêm vào playlist

    // 1. Lấy danh sách Playlist Public (Mục 2.7)
    // URL: GET /api/playlists/public
    @GetMapping("/public")
    public List<Playlist> getPublicPlaylists() {
        return playlistRepository.findByIsPublicTrue();
    }

    // 2. Lấy Playlist của User (Mục 2.5 - Để hiện trong BottomSheet thêm nhạc)
    // URL: GET /api/playlists/user/{userId}
    @GetMapping("/user/{userId}")
    public List<Playlist> getUserPlaylists(@PathVariable Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    // 3. Xem chi tiết Playlist (Lấy danh sách bài hát trong đó)
    // URL: GET /api/playlists/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistDetail(@PathVariable Long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        return playlist.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Thêm bài hát vào Playlist (Mục 2.5)
    // URL: POST /api/playlists/{playlistId}/add-song/{songId}
    @PostMapping("/{playlistId}/add-song/{songId}")
    public ResponseEntity<String> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        Optional<Song> songOpt = songRepository.findById(songId);

        if (playlistOpt.isPresent() && songOpt.isPresent()) {
            Playlist playlist = playlistOpt.get();
            Song song = songOpt.get();

            // Thêm bài hát vào list
            playlist.getSongs().add(song);
            playlistRepository.save(playlist); // Lưu xuống DB

            return ResponseEntity.ok("Đã thêm bài hát vào playlist thành công!");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy Playlist hoặc Bài hát");
        }
    }
}