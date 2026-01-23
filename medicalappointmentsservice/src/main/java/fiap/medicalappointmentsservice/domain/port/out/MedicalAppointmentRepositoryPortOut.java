package fiap.medicalappointmentsservice.domain.port.out;

import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;

import java.util.List;
import java.util.Optional;


public interface MedicalAppointmentRepositoryPortOut {

    void flush();

    void save(MedicalAppointmentEntity medicalAppointment);

    void update(Long id, String status, String appointmentDate);

    Optional<MedicalAppointmentEntity> findById(Long id);
    Optional<List<MedicalAppointmentEntity>> findAll();

    List<MedicalAppointmentEntity> findByPatient(String patient);
    List<MedicalAppointmentEntity> findFutureByPatient(String patient, String date);

}
