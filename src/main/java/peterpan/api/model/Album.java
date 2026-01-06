package peterpan.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // Tên Album

    @Column(name = "artist_name")
    private String artistName;  // Tên nghệ sĩ

    @Column(name = "image_url")
    private String imageUrl;    // Link ảnh bìa

    // Constructor rỗng (Bắt buộc cho JPA)
    public Album() {
    }

    // Constructor đầy đủ
    public Album(String name, String artistName, String imageUrl) {
        this.name = name;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}