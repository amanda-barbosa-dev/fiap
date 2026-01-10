package fiap.medicalappointmentsservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalAppointment {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String patient;
    private String doctor;
    private String medicalSpecialty;
    private String status;
    private String appointmentDate;
    private String phoneNumber;
    private boolean isRescheduled;

}
