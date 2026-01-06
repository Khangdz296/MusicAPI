package peterpan.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Import quan trọng
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // Tên Album

    // 👇 THAY ĐỔI QUAN TRỌNG: Dùng Object Artist thay vì String
    @ManyToOne
    @JoinColumn(name = "artist_id") // Tạo cột khóa ngoại artist_id
    // Khi lấy Album, lấy luôn thông tin Artist, nhưng chặn list con của Artist để tránh loop
    @JsonIgnoreProperties({"songs", "albums"})
    private Artist artist;

    @Column(name = "image_url")
    private String imageUrl;    // Link ảnh bìa

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("album") // Chặn loop ngược lại từ Song lên Album
    private List<Song> songs;

    // 1. Constructor rỗng (Bắt buộc cho JPA)
    public Album() {
    }

    // 2. Constructor đầy đủ (Đã sửa tham số artistName thành object artist)
    public Album(String name, Artist artist, String imageUrl) {
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // 👇 Sửa Getter: Trả về Object Artist
    public Artist getArtist() { return artist; }
    // 👇 Sửa Setter: Nhận vào Object Artist
    public void setArtist(Artist artist) { this.artist = artist; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }
}