package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.rmi.server.UID;
import java.util.UUID;

public class Ticket {
    @JsonIgnore
    UUID token;
    Seat s;
    @JsonIgnore
    boolean booked;
//    boolean availablity;

    public UUID getToken() {
        return token;
    }


//    public void setAvailablity(boolean availablity) {
//        this.availablity = availablity;
//    }

    public void setToken() {
        this.token = UUID.randomUUID();
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public void setS(Seat s) {
        this.s = s;
    }

    public Seat getS() {
        return s;
    }

    public Ticket(Seat s) {
        this.s = s;
        this.token = UUID.randomUUID();
    }

    public Ticket() {
    }
}
