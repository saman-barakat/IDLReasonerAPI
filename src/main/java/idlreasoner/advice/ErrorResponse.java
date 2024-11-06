package idlreasoner.advice;

public class ErrorResponse {
    private String message;
    private String details;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
