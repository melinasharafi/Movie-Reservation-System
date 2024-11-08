package movieReservationSystem.model;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private int userId;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private int movieId;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    // Getters and Setters
    // Constructors (default and parameterized)
    public Reservation(int userId, int movieId, int seatNumber) {
        this.userId = userId;
        this.movieId = movieId;
        this.seatNumber = seatNumber;
    }

    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", userId=" + userId +
                ", movieId=" + movieId +
                ", seatNumber=" + seatNumber +
                '}';
    }
}

