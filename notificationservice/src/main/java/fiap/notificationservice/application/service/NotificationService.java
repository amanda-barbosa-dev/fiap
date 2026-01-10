package fiap.notificationservice.application.service;


import fiap.notificationservice.domain.model.MedicalAppointmentEvent;
import fiap.notificationservice.domain.port.out.WathsappSenderPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService  {

    private final WathsappSenderPortOut wathsappSenderPortOut;

    public void processEvent(MedicalAppointmentEvent event) {

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


        wathsappSenderPortOut.sendNotification(event.poneNumber(), message);
    }




}
