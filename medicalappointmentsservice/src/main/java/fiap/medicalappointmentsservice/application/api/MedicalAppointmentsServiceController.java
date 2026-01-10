package fiap.medicalappointmentsservice.application.api;


import fiap.medicalappointmentsservice.domain.dto.CreateAppointmentDto;
import fiap.medicalappointmentsservice.domain.dto.UpdateAppointmentDto;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.in.SchedulerServicePortIn;
import fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalAppointments")
@RequiredArgsConstructor
public class MedicalAppointmentsServiceController {

    private final SchedulerServicePortIn service;

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<MedicalAppointment> createMedicalAppointment(@RequestHeader String authorization, @Valid @RequestBody CreateAppointmentDto createAppointmentRequest) {
        return ResponseEntity
                .status(201)
                .body(service.createMedicalAppointment(createAppointmentRequest));
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<MedicalAppointment> updateMedicalAppointment(@RequestHeader String authorization, @PathVariable Long id, @RequestBody UpdateAppointmentDto updateAppointmentRequest, @RequestParam(value = "status", required = false) AppointmentStatus status) {
        return ResponseEntity.ok(service.updateMedicalAppointment(id, updateAppointmentRequest, status));
    }


}
