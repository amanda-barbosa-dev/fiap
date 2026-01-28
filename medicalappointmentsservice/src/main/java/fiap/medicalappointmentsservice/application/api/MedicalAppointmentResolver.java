package fiap.medicalappointmentsservice.application.api;

import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.out.MedicalAppointmentRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicalAppointmentResolver {

    private final MedicalAppointmentRepositoryPortOut repository;

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'PATIENT')")
    public List<MedicalAppointment> allAppointmentsForPatient(String patient) {
        return repository.findByPatient(patient);
    }
    @PreAuthorize("hasRole('PATIENT')")
    public List<MedicalAppointment> futureAppointmentsForPatient(String patient) {
        return repository.findFutureByPatient(patient);
    }

}
