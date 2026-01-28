package fiap.medicalappointmentsservice.domain.port.out;

import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;

public interface EventProducerPortOut {
    void sendEvent(String topic, MedicalAppointment event);
}
