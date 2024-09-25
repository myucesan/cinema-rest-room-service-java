package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.condition.ConditionsReportEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class CinemaController {
    private final Cinema cinema;
    private final ConditionsReportEndpoint conditionsReportEndpoint;

    @Autowired
    public CinemaController(Cinema cinema, ConditionsReportEndpoint conditionsReportEndpoint) {
        this.cinema = cinema;
        this.conditionsReportEndpoint = conditionsReportEndpoint;
    }

    @GetMapping("/seats")
    public Map<String, Object> getSeats() {
        Map<String, Object> response = new HashMap<>();
        response.put("rows", cinema.getRows());
        response.put("columns", cinema.getColumns());
        response.put("seats", cinema.getSeats());
        return response;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> postPurchase(@RequestBody Seat seatRequest) {
        Seat seat = cinema.getSeat(seatRequest.getRow(), seatRequest.getColumn());
        ;
        if (seat == null) {
            Map<String, Object> errorResponse = new ConcurrentHashMap<>();
            errorResponse.put("error", "The number of a row or a column is out of bounds!");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (seat.isBooked()) {
            Map<String, Object> errorResponse = new ConcurrentHashMap<>() {
            };
            errorResponse.put("error", "The ticket has been already purchased!");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if (seatRequest.getRow() < 1 || seatRequest.getRow() > 9 || seatRequest.getColumn() < 1 || seatRequest.getColumn() > 9) {
            Map<String, Object> errorResponse = new ConcurrentHashMap<>();
            errorResponse.put("error", "The number of a row or a column is out of bounds!");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        seat.setBooked(true);
        Map<String, Object> pMap = new ConcurrentHashMap<>();
        Map<String, Object> ticket = new ConcurrentHashMap<>();
        ticket.put("row", seat.getRow());
        ticket.put("column", seat.getColumn());
        ticket.put("price", seat.getPrice());
        Ticket t = new Ticket(seat);
        pMap.put("token", t.getToken().toString());
        pMap.put("ticket", ticket);
        cinema.addTicket(t);

        return ResponseEntity.ok().body(pMap);
    }

//    @PostMapping("/return")
//    public ResponseEntity<Map<String, Object>> postReturn(@RequestBody ReturnTicketRequest r) {
//        Ticket ticket = cinema.getTicket(r.getToken());
//        System.out.println("Real token: " + ticket.getToken());
//        System.out.println("Intercepted token:" + r.getToken());
//        if (ticket == null || !r.getToken().equals(ticket.getToken())) {
//            Map<String, Object> errorResponse = new ConcurrentHashMap<>() {
//            };
//            errorResponse.put("error", "Wrong token!");
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        } else {
//            Map<String, Object> response = new ConcurrentHashMap<>();
//            response.put("returned_ticket", ticket);
//            // Here you should also update the seat to be unbooked
//            cinema.getSeat(ticket.getS().getRow(), ticket.getS().getColumn()).setBooked(false);
//            // Remove the ticket from the cinema
//            cinema.removeTicket(ticket);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//    }

    @PostMapping("/return")
    public ResponseEntity<Map<String, Object>> postReturn(@RequestBody Map<String, String> request) {
        try {
            String tokenString = request.get("token");
            if (tokenString == null) {
                throw new IllegalArgumentException("Token is missing");
            }

            UUID token = UUID.fromString(tokenString);
            Ticket ticket = cinema.getTicket(token);

            if (ticket == null) {
                Map<String, Object> errorResponse = new ConcurrentHashMap<>();
                errorResponse.put("error", "Wrong token!");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else {
                Map<String, Object> response = new ConcurrentHashMap<>();
                Map<String, Object> body = new ConcurrentHashMap<>();
                body.put("row", ticket.getS().getRow());
                body.put("column", ticket.getS().getColumn());
                body.put("price", ticket.getS().price);

                response.put("ticket", body);
                cinema.getSeat(ticket.getS().getRow(), ticket.getS().getColumn()).setBooked(false);
                cinema.removeTicket(ticket);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new ConcurrentHashMap<>();
            errorResponse.put("error", "Invalid token format");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log the exception here
            e.printStackTrace();
            Map<String, Object> errorResponse = new ConcurrentHashMap<>();
            errorResponse.put("error", "An unexpected error occurred");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(@RequestParam(required = false) String password) {
        if (password == null || !password.equals("super_secret")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "The password is wrong!");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        // Replace these values with actual logic to fetch statistics
        int totalIncome = cinema.getTotalIncome(); // Implement logic to calculate total income
        int availableSeats = cinema.getAllAvailableSeats(); // Implement logic to count available seats
        int purchasedTickets = cinema.getTotalTicketsPurchased(); // Implement logic to count purchased tickets

        Map<String, Object> stats = new HashMap<>();
        stats.put("income", totalIncome);
        stats.put("available", availableSeats);
        stats.put("purchased", purchasedTickets);

        return ResponseEntity.ok(stats);
    }
}