package fiap.medicalappointmentsservice.domain.port.in;

import fiap.medicalappointmentsservice.domain.dto.CreateAppointmentDto;
import fiap.medicalappointmentsservice.domain.dto.UpdateAppointmentDto;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus;

public interface SchedulerServicePortIn {
    MedicalAppointment createMedicalAppointment(CreateAppointmentDto createAppointmentRequest);
    MedicalAppointment updateMedicalAppointment(Long id, UpdateAppointmentDto updateAppointmentRequest, AppointmentStatus appointmentDate);
}
