package daniel.caixa;

import daniel.caixa.Service.VehicleService;
import daniel.caixa.dto.VehicleRequest;
import daniel.caixa.dto.VehicleResponse;
import daniel.caixa.entity.VehicleStatus;
import daniel.caixa.repository.VehicleRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class VehicleIntegrationTests {

    @InjectMock
    VehicleRepository vehicleRepository;

    @InjectMock
    VehicleService vehicleService;

    //Testar se @GET 'listall' permite acesso à user
    @Test
    @TestSecurity(user = "myuser", roles = "user")
    void shouldListAllVehiclesFromDatabase() {
        RestAssured.given()
                .get("/vehicles")
                .then()
                .statusCode(200);
    }

    //Testar se @GET 'findById' busca vehicles por ID para 'user'
    @Test
    @TestSecurity(user = "myuser", roles = "user")
    void shouldFindById() {
        Long vehicleId = 1L;

        // Simula retorno do serviço
        VehicleResponse mockResponse = new VehicleResponse();
        mockResponse.setId(vehicleId);
        mockResponse.setStatus(VehicleStatus.AVAILABLE);
        mockResponse.setModel("Onix");
        mockResponse.setBrand("Chevrolet");

        Mockito.when(vehicleService.findById(vehicleId)).thenReturn(mockResponse);

        RestAssured.given()
                .pathParam("id", vehicleId)
                .get("/vehicles/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(vehicleId.intValue()))
                .body("status", equalTo("AVAILABLE"))
                .body("model", equalTo("Onix"))
                .body("brand", equalTo("Chevrolet"));
    }

    //Testar se @POST cria veiculo com sucesso
    @Test
    @TestSecurity(user = "myuser", roles = "admin")
    void shouldCreateVehicleSuccessfully() {
        VehicleRequest request = new VehicleRequest();
        request.setModel("Onix");
        request.setBrand("Chevrolet");
        request.setEngine("1.0L");
        request.setYear(2015);
        request.setStatus(VehicleStatus.AVAILABLE);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/vehicles")
                .then()
                .statusCode(201);
    }

    //Testar se @DELETE exclui veiculo com sucesso
    @Test
    @TestSecurity(user = "adminUser", roles = "admin")
    void shouldDeleteVehicleSuccessfully() {
        Long vehicleId = 4L; // Certifique-se que esse ID existe no test.sql

        RestAssured.given()
                .pathParam("id", vehicleId)
                .delete("/vehicles/{id}")
                .then()
                .statusCode(204); // No Content
    }

}
