package fiap.medicalappointmentsservice.shared.validator;

import fiap.medicalappointmentsservice.application.service.mapper.MedicalAppointmentMapper;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.out.MedicalAppointmentRepositoryPortOut;
import fiap.medicalappointmentsservice.domain.port.out.UserRepositoryPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.UserEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Data
@RequiredArgsConstructor
public class MedicalAppointmentValidator {

    private final MedicalAppointmentRepositoryPortOut medicalAppointmentRepository;
    private final UserRepositoryPortOut userRepository;
    private final MedicalAppointmentMapper mapper;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    LocalDateTime checkedCreateDate;
    LocalDateTime checkedUpdateDate;
    String checkedPatient;
    String checkedDoctor;
    String checkedMedicalSpecialty;
    String checkedAppointmentDate;
    String checkedPhoneNumber;
    MedicalAppointmentEntity entity;
    Long checkedId;
    Long id;



    public MedicalAppointment validateUpdateMedicalAppointment(MedicalAppointment medicalAppointment) throws IllegalArgumentException {


        Long id = medicalAppointment.getId();
        String status = medicalAppointment.getStatus();



            entity = findMedicalAppointmentById(id);

             checkedCreateDate = entity.getCreateDate();
             checkedUpdateDate = LocalDateTime.now();
             checkedPatient = entity.getPatient();
             checkedDoctor = entity.getDoctor();
             checkedMedicalSpecialty = entity.getMedicalSpecialty();
             checkedPhoneNumber = entity.getPhoneNumber();
             checkedId = entity.getId();




        if (medicalAppointment.isRescheduled()) {
            checkedAppointmentDate = entity.getAppointmentDate();
            return MedicalAppointment.builder()
                    .id(id)
                    .createDate(checkedCreateDate)
                    .updateDate(checkedUpdateDate)
                    .appointmentDate(checkedAppointmentDate)
                    .status("CANCELED")
                    .patient(checkedPatient)
                    .phoneNumber(checkedPhoneNumber)
                    .doctor(checkedDoctor)
                    .medicalSpecialty(checkedMedicalSpecialty)
                    .isRescheduled(medicalAppointment.isRescheduled())
                    .build();

        } else {
                checkedAppointmentDate = entity.getAppointmentDate();
        }

        return MedicalAppointment.builder()
                .id(id)
                .createDate(checkedCreateDate)
                .updateDate(checkedUpdateDate)
                .appointmentDate(checkedAppointmentDate)
                .patient(checkedPatient)
                .phoneNumber(checkedPhoneNumber)
                .doctor(checkedDoctor)
                .medicalSpecialty(checkedMedicalSpecialty)
                .status(status)
                .isRescheduled(medicalAppointment.isRescheduled())
                .build();
    }




    public MedicalAppointment validadeCreateMedicalAppointment(MedicalAppointment medicalAppointment) {


        String status = medicalAppointment.getStatus();
        id = medicalAppointment.getId();

        LocalDateTime createDate = LocalDateTime.now();
         checkedPatient = isValidPatientName(medicalAppointment.getPatient());
         checkedDoctor = isValidDoctorName(medicalAppointment.getDoctor());
         checkedMedicalSpecialty = isValidDoctorMedicalSpecialty(checkedDoctor, medicalAppointment.getMedicalSpecialty());
         checkedAppointmentDate = isValidAppointmentDate(medicalAppointment.getAppointmentDate());

        checkScheduleConflict(checkedDoctor, checkedPatient, checkedAppointmentDate);

        return MedicalAppointment.builder()
                .createDate(createDate)
                .updateDate(createDate)
                .appointmentDate(checkedAppointmentDate)
                .patient(checkedPatient)
                .phoneNumber(medicalAppointment.getPhoneNumber())
                .doctor(checkedDoctor)
                .medicalSpecialty(checkedMedicalSpecialty)
                .status(status)
                .build();

    }

    public  void checkScheduleConflict(String checkedDoctor, String checkedPatient, String checkedAppointmentDate){


        List<MedicalAppointmentEntity> medicalAppointmentEntityList = findAllMedicalAppointments();

        if (!medicalAppointmentEntityList.isEmpty()) {
            medicalAppointmentEntityList.forEach(foundedEntity ->
                checkAvailability(checkedDoctor, checkedPatient, checkedAppointmentDate, foundedEntity)
            );
        }
    }

    public void checkAvailability(String checkedDoctor, String checkedPatient, String validAppointmentDate, MedicalAppointmentEntity entity) {


        String entityDoctorName = entity.getDoctor();
        String entityPatientName = entity.getPatient();
        Long entityId = entity.getId();

        String entityStatus = Optional.of(entity)
                .map(MedicalAppointmentEntity::getStatus)
                .orElse("SCHEDULED");

        String entityAppointmentDate = entity.getAppointmentDate();


        LocalDateTime localDateTimeAppointmentDate = LocalDateTime.parse(entityAppointmentDate, formatter);
        LocalDateTime localDateTimeValidAppointmentDate = LocalDateTime.parse(validAppointmentDate, formatter);

        if (!Objects.equals(entityId, id) && entityStatus.equals("SCHEDULED")){
            checkDoctorPatientConflict(checkedDoctor, checkedPatient, validAppointmentDate, entityDoctorName, entityPatientName, entityAppointmentDate, localDateTimeAppointmentDate, localDateTimeValidAppointmentDate);
            checkDoctorAvailability(checkedDoctor, validAppointmentDate, entityDoctorName, entityAppointmentDate);
            checkPatientAvailability(checkedPatient, validAppointmentDate, entityPatientName, entityAppointmentDate);
        }
    }

    private void checkDoctorPatientConflict(String checkedDoctor, String checkedPatient, String validAppointmentDate, String entityDoctorName, String entityPatientName, String entityAppointmentDate, LocalDateTime localDateTimeAppointmentDate, LocalDateTime localDateTimeValidAppointmentDate) {
        if (entityDoctorName.equalsIgnoreCase(checkedDoctor) && entityPatientName.equalsIgnoreCase(checkedPatient)) {
            if (entityAppointmentDate.equals(validAppointmentDate)) {
                throw new IllegalArgumentException("Patient already has an appointment with this doctor at: " + validAppointmentDate);
            }
            if (localDateTimeAppointmentDate.toLocalDate().equals(localDateTimeValidAppointmentDate.toLocalDate())) {
                throw new IllegalArgumentException("Patient already has an appointment with this doctor on the same day at: " + entityAppointmentDate.substring(11, 16));
            }
            if (localDateTimeValidAppointmentDate.isBefore(localDateTimeAppointmentDate.plusDays(30)) && localDateTimeValidAppointmentDate.isAfter(localDateTimeAppointmentDate)) {
                throw new IllegalArgumentException("Patient already has an appointment with this doctor within 30 days.");
            }
        }
    }

    private void checkDoctorAvailability(String checkedDoctor, String validAppointmentDate, String entityDoctorName, String entityAppointmentDate) {
        if (entityDoctorName.equalsIgnoreCase(checkedDoctor) && entityAppointmentDate.equals(validAppointmentDate)) {
            throw new IllegalArgumentException("Doctor is not available at: " + validAppointmentDate);
        }
    }

    private void checkPatientAvailability(String checkedPatient, String validAppointmentDate, String entityPatientName, String entityAppointmentDate) {
        if (entityPatientName.equalsIgnoreCase(checkedPatient) && entityAppointmentDate.equals(validAppointmentDate)) {
            throw new IllegalArgumentException("Patient is not available at: " + validAppointmentDate);
        }
    }


    private String isValidAppointmentDate(String medicalAppointmentDate) throws IllegalArgumentException {

        LocalDateTime appointmentDateParsed = LocalDateTime.parse(medicalAppointmentDate, formatter);

        if (appointmentDateParsed.isAfter(LocalDateTime.now())) {
            return medicalAppointmentDate;
        } else {
            throw new IllegalArgumentException("Appointment date must be in the future");
        }
    }


    private String isValidPatientName(String patient) {
        UserEntity patientEntity = userRepository.findByNameAndRole(patient, "PATIENT")
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return patientEntity.getName();

    }

    private String isValidDoctorName(String doctor) {
        UserEntity doctorEntity = userRepository.findByNameAndRole(doctor, "DOCTOR")
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return doctorEntity.getName();


    }

    private String isValidDoctorMedicalSpecialty(String doctor, String medicalSpecialty) throws IllegalArgumentException {
        UserEntity doctorEntity = userRepository.findByNameAndMedicalSpecialty(doctor, medicalSpecialty)
                .orElseThrow(() -> new IllegalArgumentException("Medical speciality is wrong"));
        return doctorEntity.getMedicalSpecialty();
    }

    private MedicalAppointmentEntity findMedicalAppointmentById(Long id) {
        return medicalAppointmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID: " + id + " not found"));
    }

    public List<MedicalAppointmentEntity> findAllMedicalAppointments() {
        return medicalAppointmentRepository.findAll().orElse(new ArrayList<>());
    }
}


