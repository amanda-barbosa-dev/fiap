package fiap.medicalappointmentsservice.infrastructure.event;

import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.out.EventProducerPortOut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements EventProducerPortOut {

    private final KafkaTemplate<String, MedicalAppointment> kafkaTemplate;


    public KafkaProducer(KafkaTemplate<String, MedicalAppointment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendEvent(String topic, MedicalAppointment event) {
        kafkaTemplate.send(topic, event);
    }
}
