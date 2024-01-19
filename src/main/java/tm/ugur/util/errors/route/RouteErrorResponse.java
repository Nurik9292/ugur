package tm.ugur.util.errors.route;

public class RouteErrorResponse {

    private String message;

    private long timestamp;

    public RouteErrorResponse(){}

    public RouteErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
