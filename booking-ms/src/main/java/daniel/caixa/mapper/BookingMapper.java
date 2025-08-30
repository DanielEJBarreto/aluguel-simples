package daniel.caixa.mapper;

import daniel.caixa.dto.BookingRequest;
import daniel.caixa.dto.BookingResponse;
import daniel.caixa.entity.Booking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookingMapper {

    // Agora recebe o customerId como argumento externo (vindo do JWT)
    public Booking toEntity(BookingRequest dto, String customerId) {
        Booking b = new Booking();
        b.setVehicleId(dto.getVehicleId());
        b.setCustomerId(customerId); // vindo do token
        b.setStartDate(dto.getStartDate());
        b.setEndDate(dto.getEndDate());
        b.setStatus(dto.getStatus());
        return b;
    }

    public BookingResponse toResponse(Booking b) {
        BookingResponse dto = new BookingResponse();
        dto.setId(b.getId());
        dto.setVehicleId(b.getVehicleId());
        dto.setCustomerId(b.getCustomerId());
        dto.setStartDate(b.getStartDate());
        dto.setEndDate(b.getEndDate());
        dto.setStatus(b.getStatus());
        return dto;
    }
}