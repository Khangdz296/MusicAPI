package peterpan.api.model;



import peterpan.api.model.Song;
import peterpan.api.model.User;
import peterpan.api.repository.SongRepository;
import peterpan.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class Favorite {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    // 1. Thêm bài hát vào danh sách yêu thích
    public boolean addSongToFavorites(Long userId, Long songId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Song> songOpt = songRepository.findById(songId);

        if (userOpt.isPresent() && songOpt.isPresent()) {
            User user = userOpt.get();
            Song song = songOpt.get();

            user.getFavoriteSongs().add(song); // Thêm vào Set
            userRepository.save(user); // Lưu lại
            return true;
        }
        return false;
    }

    // 2. Xóa bài hát khỏi danh sách yêu thích
    public boolean removeSongFromFavorites(Long userId, Long songId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Song> songOpt = songRepository.findById(songId);

        if (userOpt.isPresent() && songOpt.isPresent()) {
            User user = userOpt.get();
            Song song = songOpt.get();

            user.getFavoriteSongs().remove(song); // Xóa khỏi Set
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // 3. Lấy danh sách bài hát yêu thích của User
    public Set<Song> getFavoriteSongs(Long userId) {
        return userRepository.findById(userId)
                .map(User::getFavoriteSongs)
                .orElse(null);
    }

    // 4. Kiểm tra xem bài hát đã được like chưa
    public boolean isSongLiked(Long userId, Long songId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getFavoriteSongs().stream()
                    .anyMatch(song -> song.getId().equals(songId));
        }
        return false;
    }
}