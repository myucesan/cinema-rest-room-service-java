package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;


public class Seat {
    Integer row;
    Integer column;
    Integer price;
    @JsonIgnore
    boolean booked;

    @Bean
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
    @Bean
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    @Bean
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public Seat() {}
    public Seat(int row, int column, int price) {
        this.row = row;
        this.column = column;
        this.booked = false;
        this.price = price;
    }
}
