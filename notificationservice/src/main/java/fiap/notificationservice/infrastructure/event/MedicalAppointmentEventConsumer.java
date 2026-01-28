package fiap.notificationservice.infrastructure.event;

import fiap.notificationservice.domain.port.in.NotificationSevicePortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalAppointmentEventConsumer {

 private final NotificationSevicePortIn service;

    @KafkaListener(topics = "medical-appointments-topic", groupId = "medical-appointment-notification-group")
    public void receiveEvent(String event) {
        service.processEvent(event);
    }

}
