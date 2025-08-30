package daniel.caixa.repository;

import daniel.caixa.entity.Booking;
import daniel.caixa.entity.BookingStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {

    public Booking findActiveByVehicleId(Long vehicleId) {
        return find("vehicleId = ?1 and status = ?2", vehicleId, BookingStatus.RENTED)
                .firstResult();
    }

    public boolean hasDateConflict(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return count("vehicleId = ?1 and status = ?2 and startDate <= ?3 and endDate >= ?4",
                vehicleId, BookingStatus.RENTED, endDate, startDate) > 0;
    }

    public List<Booking> findByCustomerId(String customerId) {
        return list("customerId", customerId);
    }

    public Optional<Booking> findByIdAndCustomer(Long id, String customerId) {
        return find("id = ?1 and customerId = ?2", id, customerId).firstResultOptional();
    }

}

