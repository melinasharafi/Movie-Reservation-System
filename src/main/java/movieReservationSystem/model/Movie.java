package movieReservationSystem.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "show_time", nullable = false)
    private Timestamp showTime;

    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    // Getters and Setters
    // Constructors (default and parameterized)

    public Movie(String title, String description, Timestamp showTime, String genre, int capacity, int availableSeats) {
        this.title = title;
        this.description = description;
        this.showTime = showTime;
        this.genre = genre;
        this.capacity = capacity;
        this.availableSeats = availableSeats;
    }

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getShowTime() {
        return showTime;
    }

    public void setShowTime(Timestamp showTime) {
        this.showTime = showTime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", showTime=" + showTime +
                ", genre='" + genre + '\'' +
                ", capacity=" + capacity +
                ", availableSeats=" + availableSeats +
                '}';
    }
}

