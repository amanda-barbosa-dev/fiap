package fiap.medicalappointmentsservice.infrastructure.persistence.repository;


import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.out.MedicalAppointmentRepositoryPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class MedicalAppointmentRepository implements MedicalAppointmentRepositoryPortOut {

    private final JpaMedicalAppointmentRepository jpaMedicalAppointmentRepository;

    @Override
    public void save(MedicalAppointmentEntity medicalAppointment) {
        jpaMedicalAppointmentRepository.save(medicalAppointment);
    }

    @Override
    public void update(Long id, String status, String appointmentDate) {
        jpaMedicalAppointmentRepository.update( id, status.toUpperCase(), appointmentDate);
    }

    @Override
    public Optional<MedicalAppointmentEntity> findById(Long id) {
        return jpaMedicalAppointmentRepository.findById(id);
    }

    @Override
    public Optional<List<MedicalAppointmentEntity>> findAll() {
       return Optional.of(jpaMedicalAppointmentRepository.findAll());
    }

    @Override
    public List<MedicalAppointmentEntity> findByPatient(String patient) {
        return jpaMedicalAppointmentRepository.findByPatient(patient);
    }

    @Override
    public List<MedicalAppointmentEntity> findFutureByPatient(String patient) {
        return jpaMedicalAppointmentRepository.findFutureByPatient(patient);
    }
}
