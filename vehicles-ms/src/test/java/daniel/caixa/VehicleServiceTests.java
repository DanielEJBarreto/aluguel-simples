package daniel.caixa;

import daniel.caixa.Entity.Vehicle;
import daniel.caixa.Entity.VehicleStatus;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTests {

    //Testar todas as regras de negócio da classe Vehicle (mudança de status)
    private Vehicle mockAvailableVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        return vehicle;
    }

    //Um veículo só pode ser alterado para RENTED se seu status atual for AVAILABLE
//    @Test
//    public void testVehicleAlterToRENTEDifAVAILABLE() {
//
//
//
//    }
}
