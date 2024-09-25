package cinema;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CinemaConfig {
    @Bean
    public Cinema cinema() {
        return new Cinema(9, 9, seatList()); // Example values for rows, columns, and seats
    }

    @Bean
    public List<Seat> seatList() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                if (i <= 4) {
                    seats.add(new Seat(i, j, 10));
                } else {
                    seats.add(new Seat(i, j, 8));
                }
            }
        }
//        return List.of(new Seat(1, 1), new Seat(1, 2), new Seat(1, 3));
        return seats;
    }



}
