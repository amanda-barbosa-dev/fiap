package fiap.notificationservice.domain.port.in;

import fiap.notificationservice.domain.model.MedicalAppointmentEvent;

public interface NotificationSevicePortIn {

    void processEvent(String eventPayload);
}
