package fiap.medicalappointmentsservice.domain.port.out;

public interface EventProducerPortOut {
    void sendEvent(String topic, String mensagem);
}
