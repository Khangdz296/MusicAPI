package peterpan.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String imageUrl;
    private String fileUrl;
    private int duration;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 👇 MỚI: Thêm biến views (Mặc định là 0)
    @Column(name = "views")
    private int views = 0;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Song() {
    }

    // Constructor cập nhật (Thêm views vào cuối nếu thích, hoặc không cần cũng được vì JPA tự set)
    public Song(Long id, String title, String artist, String imageUrl, String fileUrl, int duration, boolean isFavorite, LocalDateTime createdAt, Category category, int views) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.fileUrl = fileUrl;
        this.duration = duration;
        this.isFavorite = isFavorite;
        this.createdAt = createdAt;
        this.category = category;
        this.views = views;
    }

    // --- Getters & Setters cũ giữ nguyên ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // 👇 MỚI: Getter & Setter cho Views (Bắt buộc phải có)
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
}