package fiap.notificationservice.application.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.notificationservice.domain.model.MedicalAppointmentEvent;
import fiap.notificationservice.domain.port.in.NotificationSevicePortIn;
import fiap.notificationservice.domain.port.out.WhatsappSenderPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationSevicePortIn {

    private final WhatsappSenderPortOut whatsappSenderPortOut;

    public void processEvent(String eventPayload) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            MedicalAppointmentEvent event = mapper.readValue(eventPayload, MedicalAppointmentEvent.class);
            String message;

            if (event.status().equals("SCHEDULED") && event.isRescheduled()) {
                message = String.format(
                        "Reminder: Your consult is rescheduled %s. ID: %s",
                        event.appointmentDate(), event.id()
                );
            } else if (event.status().equals("SCHEDULED")){
                message = String.format(
                        "Reminder: Your consult is scheduled %s. ID: %s",
                        event.appointmentDate(), event.id()
                );
            } else {
                message = String.format(
                        "Reminder: Your consult is scheduled %s. ID: %s",
                        event.appointmentDate(), event.id()
                );

            }
            whatsappSenderPortOut.sendNotification(event.poneNumber(), message);

        } catch (IOException e) {
            throw new RuntimeException("Error processing event payload: {}" + e.getMessage());
        }

    }

}
