package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cinema {
    Integer rows;
    Integer columns;
    List<Seat> seats;
    List<Ticket> tickets = new ArrayList<>();

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket t) {
        tickets.add(t);
    }

    public void removeTicket(Ticket t) {
        tickets.remove(t);
    }

    @Bean
    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Bean
    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Cinema(Integer rows, Integer columns, List<Seat> seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
    }

    public Seat getSeat(int row, int column) {
        for (Seat seat : getSeats()) {
            if (seat.getRow() == row && seat.getColumn() == column) {
                return seat;
            }
        }
        return null; // Seat not found
    }

    public Ticket getTicket (UUID token) {
        for (Ticket ticket : getTickets()) {
            if (ticket.getToken().equals(token)) {
                return ticket;
            }
        }

        return null;
    }

    public Integer getTotalIncome() {
        int income = 0;
        for (Ticket t : getTickets()) {
            income += t.getS().price;
        }

        return income;
    }

    public Integer getAllAvailableSeats() {
        int availableSeats = 0;
        for (Seat s : getSeats()) {
            if (!s.isBooked()) {
                availableSeats++;
            }
        }

        return availableSeats;
    }

    public Integer getTotalTicketsPurchased() {
        int purchasedTickets = 0;
        for (Ticket t : getTickets()) {
            purchasedTickets++;
        }
        return purchasedTickets;
    }
}
