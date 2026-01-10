package fiap.medicalappointmentsservice.infrastructure.event;

import fiap.medicalappointmentsservice.domain.port.out.EventProducerPortOut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements EventProducerPortOut {

    private final KafkaTemplate<String, String> kafkaTemplate;


    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendEvent(String topic, String mesage) {
        kafkaTemplate.send(topic, mesage);
    }
}
