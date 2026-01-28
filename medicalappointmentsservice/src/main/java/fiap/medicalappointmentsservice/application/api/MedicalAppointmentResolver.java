package fiap.medicalappointmentsservice.application.api;

import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.in.SchedulerServicePortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalAppointmentResolver {

    private final SchedulerServicePortIn service;


    @QueryMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'PATIENT')")
    public List<MedicalAppointment> allAppointmentsForPatient(@Argument String patient) {
        return service.allAppointmentsForPatient(patient);
    }

    @QueryMapping
    @PreAuthorize("hasRole('PATIENT')")
    public List<MedicalAppointment> futureAppointmentsForPatient(@Argument String patient) {
        return service.futureAppointmentsForPatient(patient);
    }

}
