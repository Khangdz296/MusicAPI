package peterpan.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import peterpan.api.model.Playlist;
import peterpan.api.model.Song;
import peterpan.api.model.User;
import peterpan.api.repository.PlaylistRepository;
import peterpan.api.repository.SongRepository;
import peterpan.api.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SongRepository songRepository;

    @PostMapping("/user/{userId}")
    public Playlist createPlaylist(@PathVariable Long userId, @RequestBody Playlist playlistData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        playlistData.setUser(user); // Gán user vào playlist
        // Mặc định lúc tạo chưa có bài hát nào, hoặc list rỗng

        return playlistRepository.save(playlistData);
    }

    @GetMapping("/user/{userId}")
    public List<Playlist> getPlaylistsByUser(@PathVariable Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    @PostMapping("/{playlistId}/add-song/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // Thêm bài hát vào list (nếu chưa tồn tại)
        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            return ResponseEntity.ok(playlistRepository.save(playlist));
        }

        return ResponseEntity.ok(playlist); // Trả về playlist cũ nếu bài hát đã có rồi
    }

    @DeleteMapping("/{playlistId}/remove-song/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // Dùng removeIf để xóa bài hát có ID tương ứng
        playlist.getSongs().removeIf(s -> s.getId().equals(songId));

        return ResponseEntity.ok(playlistRepository.save(playlist));
    }
}