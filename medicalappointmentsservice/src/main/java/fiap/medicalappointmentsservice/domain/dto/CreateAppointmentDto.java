package fiap.medicalappointmentsservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentDto {
    private Long id;
    @NotBlank(message = "Patient name must be informed.")
    private String patient;
    @NotBlank(message = "Patient phone number must be informed.")
    private String phoneNumber;
    @NotBlank(message = "Doctor name must be informed.")
    private String doctor;
    @NotBlank(message = "Doctor's medical specialty must be informed.")
    private String medicalSpecialty;
    @NotNull (message = "Appointment date must be informed.")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9])$", message = "Appointment date must be in format 'dd/MM/yyyy HH:mm'")
    private String appointmentDate;
}
