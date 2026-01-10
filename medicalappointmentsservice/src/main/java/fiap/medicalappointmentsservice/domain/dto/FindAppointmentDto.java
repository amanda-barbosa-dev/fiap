package fiap.medicalappointmentsservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindAppointmentDto {
    @NotBlank(message = "Patient must be informed")
    private String patient;
}
