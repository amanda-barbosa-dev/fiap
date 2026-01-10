package fiap.medicalappointmentsservice.infrastructure.persistence.entity;

import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="tb_medical_appointments")
@Data
public class MedicalAppointmentEntity {

    public MedicalAppointmentEntity(){}

    public MedicalAppointmentEntity(MedicalAppointment medicalAppointment) {
        this.id = this.getId();
        this.patient = medicalAppointment.getPatient().toUpperCase();
        this.phoneNumber = medicalAppointment.getPhoneNumber();
        this.createDate = medicalAppointment.getCreateDate();
        this.updateDate = medicalAppointment.getUpdateDate();
        this.doctor = medicalAppointment.getDoctor().toUpperCase();
        this.medicalSpecialty = medicalAppointment.getMedicalSpecialty().toUpperCase();
        this.status = medicalAppointment.getStatus();
        this.appointmentDate = medicalAppointment.getAppointmentDate();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String phoneNumber;
    private String patient;
    private String doctor;
    private String medicalSpecialty;
    private String status;
    private String appointmentDate;
}
