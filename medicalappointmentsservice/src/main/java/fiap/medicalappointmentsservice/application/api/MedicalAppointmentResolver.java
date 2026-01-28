package fiap.medicalappointmentsservice.application.api;

import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.in.SchedulerServicePortIn;
import fiap.medicalappointmentsservice.domain.port.out.MedicalAppointmentRepositoryPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalAppointmentResolver {

    private final SchedulerServicePortIn service;


    @QueryMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'PATIENT')")
    public List<MedicalAppointment> allAppointmentsForPatient(String patient) {
        return service.allAppointmentsForPatient(patient);
    }

    @QueryMapping
    @PreAuthorize("hasRole('PATIENT')")
    public List<MedicalAppointment> futureAppointmentsForPatient(String patient) {
        return service.futureAppointmentsForPatient(patient);
    }

}
