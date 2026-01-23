package fiap.medicalappointmentsservice.application.service;

import fiap.medicalappointmentsservice.domain.port.in.MedicalAppointmentGraphQLInterface;
import fiap.medicalappointmentsservice.domain.port.out.MedicalAppointmentRepositoryPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicalAppointmentResolver implements MedicalAppointmentGraphQLInterface {

    private final MedicalAppointmentRepositoryPortOut repository;

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'PATIENT')")
    public List<MedicalAppointmentEntity> allAppointmentsForPatient(String patient) {
        return repository.findByPatient(patient);
    }
    @PreAuthorize("hasRole('PATIENT')")
    public List<MedicalAppointmentEntity> futureAppointmentsForPatient(String patient) {
        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String date = dateNow.format(formatter);
        return repository.findFutureByPatient(patient, date);
    }

}
