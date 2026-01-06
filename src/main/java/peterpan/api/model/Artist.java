package peterpan.api.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description; // Thêm mô tả

    // 1. LIÊN KẾT 1-N VỚI BÀI HÁT
    // Một ca sĩ có nhiều bài hát. Khi xóa ca sĩ, xóa luôn bài hát (CascadeType.ALL)
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @JsonIgnore // Ngắt vòng lặp vô tận khi chuyển sang JSON
    private List<Song> songs;

    // 2. LIÊN KẾT 1-N VỚI ALBUM
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Album> albums;

    public Artist() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }

    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }
}