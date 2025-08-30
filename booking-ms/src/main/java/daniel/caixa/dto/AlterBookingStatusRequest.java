package daniel.caixa.dto;

import daniel.caixa.entity.BookingStatus;

public class AlterBookingStatusRequest {
    private Long bookingId;
    private BookingStatus status;

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

