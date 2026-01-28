package fiap.medicalappointmentsservice.domain.port.in;

import fiap.medicalappointmentsservice.application.service.mapper.MedicalAppointmentMapper;
import fiap.medicalappointmentsservice.domain.dto.CreateAppointmentDto;
import fiap.medicalappointmentsservice.domain.dto.UpdateAppointmentDto;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus;

import java.util.List;

public interface SchedulerServicePortIn {
    MedicalAppointment createMedicalAppointment(CreateAppointmentDto createAppointmentRequest);
    MedicalAppointment updateMedicalAppointment(Long id, UpdateAppointmentDto updateAppointmentRequest, AppointmentStatus appointmentDate);
    List<MedicalAppointment> allAppointmentsForPatient(String patient);
    List<MedicalAppointment> futureAppointmentsForPatient(String patient);
}
