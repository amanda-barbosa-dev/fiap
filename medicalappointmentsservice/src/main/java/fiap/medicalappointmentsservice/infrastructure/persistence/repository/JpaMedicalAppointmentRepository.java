package fiap.medicalappointmentsservice.infrastructure.persistence.repository;

import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JpaMedicalAppointmentRepository extends JpaRepository<MedicalAppointmentEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_medical_appointments SET status = :status, appointment_date = :appointmentDate WHERE id = :id", nativeQuery = true)
    void update(@Param("id") Long id, @Param("status") String status, @Param("appointmentDate") String appointmentDate);

    @Query("SELECT m FROM MedicalAppointmentEntity m WHERE m.patient = :patient")
    List<MedicalAppointmentEntity> findByPatient(String patient);

    @Query("SELECT m FROM MedicalAppointmentEntity m WHERE m.patient = :patient AND m.appointmentDate > :date")
    List<MedicalAppointmentEntity> findFutureByPatient(String patient, String date);
}
