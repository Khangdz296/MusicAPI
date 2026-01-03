package peterpan.api.model;

import jakarta.persistence.*; // Nếu dùng Spring Boot cũ (<3.0) thì sửa thành javax.persistence.*
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_public")
    private boolean isPublic;

    // Liên kết với User (Người tạo)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Liên kết với Song (Một playlist có nhiều bài hát)
    // Tự động tạo ra bảng trung gian 'playlist_songs'
    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    // Constructor, Getter, Setter
    public Playlist() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }
}