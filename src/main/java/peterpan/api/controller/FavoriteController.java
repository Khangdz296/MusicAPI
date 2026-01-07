package peterpan.api.controller;


import peterpan.api.model.Song;
import peterpan.api.model.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private Favorite favorite;

    // API: Thêm bài hát vào yêu thích
    // POST: /api/favorites/{userId}/add/{songId}
    @PostMapping("/{userId}/add/{songId}")
    public ResponseEntity<String> addToFavorites(@PathVariable Long userId, @PathVariable Long songId) {
        boolean success = favorite.addSongToFavorites(userId, songId);
        if (success) {
            return ResponseEntity.ok("Added to favorites");
        }
        return ResponseEntity.badRequest().body("Failed to add");
    }

    // API: Xóa bài hát khỏi yêu thích
    // DELETE: /api/favorites/{userId}/remove/{songId}
    @DeleteMapping("/{userId}/remove/{songId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long userId, @PathVariable Long songId) {
        boolean success = favorite.removeSongFromFavorites(userId, songId);
        if (success) {
            return ResponseEntity.ok("Removed from favorites");
        }
        return ResponseEntity.badRequest().body("Failed to remove");
    }

    // API: Lấy danh sách bài hát yêu thích
    // GET: /api/favorites/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<Set<Song>> getUserFavorites(@PathVariable Long userId) {
        Set<Song> songs = favorite.getFavoriteSongs(userId);
        if (songs != null) {
            return ResponseEntity.ok(songs);
        }
        return ResponseEntity.notFound().build();
    }

    // API: Kiểm tra trạng thái Like
    @GetMapping("/{userId}/check/{songId}")
    public ResponseEntity<Boolean> checkIsLiked(@PathVariable Long userId, @PathVariable Long songId) {
        boolean isLiked = favorite.isSongLiked(userId, songId);
        return ResponseEntity.ok(isLiked);
    }
}