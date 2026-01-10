package fiap.notificationservice.domain.model;


import java.time.LocalDateTime;


public record MedicalAppointmentEvent (
    Long id,
    LocalDateTime createDate,
    LocalDateTime updateDate,
    String patient,
    String poneNumber,
    String doctor,
    String medicalSpecialty,
    String status,
    String appointmentDate,
    boolean isRescheduled

){}
