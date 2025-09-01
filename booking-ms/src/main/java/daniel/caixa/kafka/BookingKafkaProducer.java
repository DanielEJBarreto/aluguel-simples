package daniel.caixa.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import daniel.caixa.entity.Booking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class BookingKafkaProducer {

    @Inject
    @Channel("reserva-ativa-out")
    Emitter<String> reservaAtivaEmitter;

    @Inject
    @Channel("reserva-concluida-out")
    Emitter<String> reservaConcluidaEmitter;

    @Inject
    @Channel("reserva-cancelada-out")
    Emitter<String> reservaCanceladaEmitter;


    public void sendReservaAtiva(Booking booking) {
        reservaAtivaEmitter.send(toJson(booking));
    }

    public void sendReservaConcluida(Booking booking) {
        reservaConcluidaEmitter.send(toJson(booking));
    }

    public void sendReservaCancelada(Booking booking) {
        reservaCanceladaEmitter.send(toJson(booking));
    }

    private String toJson(Booking booking) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // para datas
            return mapper.writeValueAsString(booking);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar Booking", e);
        }
    }
}