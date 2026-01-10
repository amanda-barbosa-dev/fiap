package fiap.medicalappointmentsservice.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;




@Data
public class UpdateAppointmentDto {

    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9])$", message = "Appointment date must be in format 'dd/MM/yyyy HH:mm'")
    private String appointmentDate;
}
