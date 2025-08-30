package daniel.caixa.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String code;
    private String message;

    public ErrorResponse(LocalDateTime timestamp, String code, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
