package daniel.caixa.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

//PARA TESTAR SE AS MENSAGENS ESTÃO SENDO ENVIADAS
@ApplicationScoped
public class BookingKafkaConsumer {

    @Incoming("reserva-ativa-in")
    public void onReservaAtiva(String payload) {
        System.out.println("Mensagem recebida em reserva-ativa: " + payload);
    }

    @Incoming("reserva-concluida-in")
    public void onReservaConcluida(String payload) {
        System.out.println("Mensagem recebida em reserva-concluida: " + payload);
    }

    @Incoming("reserva-cancelada-in")
    public void onReservaCancelada(String payload) {
        System.out.println("Mensagem recebida em reserva-cancelada: " + payload);
    }
}
