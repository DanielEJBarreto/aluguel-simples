package daniel.caixa.BookingTests;

import daniel.caixa.client.VehicleAPIClient;
import daniel.caixa.dto.AlterBookingStatusRequest;
import daniel.caixa.dto.BookingRequest;
import daniel.caixa.entity.Booking;
import daniel.caixa.entity.BookingStatus;
import daniel.caixa.repository.BookingRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

@QuarkusTest
public class BookingIntegrationTests {

    @InjectMock
    @RestClient
    VehicleAPIClient vehicleAPIClient;

    @InjectMock
    BookingRepository bookingRepository;

    //Criar reserva com dados válidos
    @Test
    @TestSecurity(user = "myuser", roles = "user")
    void CreateBookingWithValidData() {
        Mockito.when(vehicleAPIClient.findVehicleById(1L))
                .thenReturn(new VehicleAPIClient.Vehicle("AVAILABLE"));

        BookingRequest bookingRequest = new BookingRequest(1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(7));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(bookingRequest)
                .post("/bookings")
                .then()
                .statusCode(201);
    }

    //Criar reserva com data inválida
    @Test
    @TestSecurity(user = "myuser", roles = "user")
    void CreateBookingWithInvalidDate(){
        Mockito.when(vehicleAPIClient.findVehicleById(1L))
                .thenReturn(new VehicleAPIClient.Vehicle("AVAILABLE"));

        BookingRequest bookingRequest = new BookingRequest(1L,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(7));
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(bookingRequest)
                .post("/bookings")
                .then()
                .statusCode(400);
    }

    //Cancelar reserva
    @Transactional
    @Test
    @TestSecurity(user = "myuser", roles = "user")
    void CancelBooking() {
        Long bookingId = 42L;

        // Simula reserva existente com status CREATED
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setVehicleId(1L);
        mockBooking.setCustomerId("myuser");
        mockBooking.setStartDate(LocalDate.now().plusDays(1));
        mockBooking.setEndDate(LocalDate.now().plusDays(7));
        mockBooking.setStatus(BookingStatus.CREATED);

        Mockito.when(bookingRepository.findByIdOptional(bookingId))
                .thenReturn(Optional.of(mockBooking));

        // Simula retorno do veículo
        Mockito.when(vehicleAPIClient.findVehicleById(1L))
                .thenReturn(new VehicleAPIClient.Vehicle("RENTED"));

        // Simula atualização de status do veículo
        Mockito.when(vehicleAPIClient.updateStatus(bookingId, new VehicleAPIClient.Vehicle("AVAILABLE")))
                .thenReturn("OK");

        // Corpo da requisição PATCH
        AlterBookingStatusRequest alterRequest = new AlterBookingStatusRequest();
        alterRequest.setStatus(BookingStatus.CANCELED);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(alterRequest)
                .patch("/bookings/" + bookingId + "/alter")
                .then()
                .statusCode(200);
    }

//Tentar cancelar reserva já cancelada
    @Test
    @TestSecurity(user = "myuser", roles = "user")
    void CancelBookingAlreadyCanceled() {
        Long bookingId = 42L;

        // Simula reserva existente com status CREATED
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setVehicleId(1L);
        mockBooking.setCustomerId("myuser");
        mockBooking.setStartDate(LocalDate.now().plusDays(1));
        mockBooking.setEndDate(LocalDate.now().plusDays(7));
        mockBooking.setStatus(BookingStatus.CANCELED);

        Mockito.when(bookingRepository.findByIdOptional(bookingId))
                .thenReturn(Optional.of(mockBooking));

        // Simula retorno do veículo
        Mockito.when(vehicleAPIClient.findVehicleById(1L))
                .thenReturn(new VehicleAPIClient.Vehicle("RENTED"));

        // Simula atualização de status do veículo
        Mockito.when(vehicleAPIClient.updateStatus(bookingId, new VehicleAPIClient.Vehicle("AVAILABLE")))
                .thenReturn("OK");

        // Corpo da requisição PATCH
        AlterBookingStatusRequest alterRequest = new AlterBookingStatusRequest();
        alterRequest.setStatus(BookingStatus.CANCELED);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(alterRequest)
                .patch("/bookings/" + bookingId + "/alter")
                .then()
                .statusCode(409);
    }

    //Finalizar reserva
//    @Transactional
//    @Test
//    void FinishBooking() {
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .pathParam("id", 3L)
//                .body("""
//                        {"status": "FINISHED"}
//                        """)
//                .patch("/bookings/{id}/alter")
//                .then()
//                .statusCode(200);
//    }
}
