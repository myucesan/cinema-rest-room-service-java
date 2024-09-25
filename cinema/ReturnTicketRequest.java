package cinema;

import java.util.UUID;

public class ReturnTicketRequest {
    private UUID token;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
