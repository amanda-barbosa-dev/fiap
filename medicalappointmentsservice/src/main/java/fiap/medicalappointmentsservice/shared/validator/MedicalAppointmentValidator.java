package fiap.medicalappointmentsservice.shared.validator;

import fiap.medicalappointmentsservice.application.service.mapper.MedicalAppointmentMapper;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.out.MedicalAppointmentRepositoryPortOut;
import fiap.medicalappointmentsservice.domain.port.out.UserRepositoryPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.UserEntity;
import fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Data
@RequiredArgsConstructor
@Slf4j
public class MedicalAppointmentValidator {

    private final String SCHEDULED = AppointmentStatus.SCHEDULED.toString();
    private final String COMPLETED = AppointmentStatus.COMPLETED.toString();
    private final String CANCELED = AppointmentStatus.CANCELED.toString();
    private final MedicalAppointmentRepositoryPortOut medicalAppointmentRepository;
    private final UserRepositoryPortOut userRepository;
    private final MedicalAppointmentMapper mapper;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");



    public MedicalAppointment validateUpdateMedicalAppointment(MedicalAppointment medicalAppointment) throws IllegalArgumentException {


        Long medicalAppointmentId = medicalAppointment.getId();
        String medicalAppointmentDate = medicalAppointment.getAppointmentDate();
        String status = medicalAppointment.getStatus();


        var entity = findMedicalAppointmentById(medicalAppointmentId);

        LocalDateTime checkedCreateDate = entity.getCreateDate();
        LocalDateTime checkedUpdateDate = LocalDateTime.now();
        String checkedPatient = entity.getPatient();
        String checkedDoctor = entity.getDoctor();
        String checkedMedicalSpeciality = entity.getMedicalSpecialty();
        String checkedAppointmentDate;

            if (medicalAppointment.isRescheduled()) {
                    checkedAppointmentDate = entity.getAppointmentDate();
                    return MedicalAppointment.builder()
                        .id(medicalAppointmentId)
                            .phoneNumber(medicalAppointment.getPhoneNumber())
                        .updateDate(checkedUpdateDate)
                        .appointmentDate(checkedAppointmentDate)
                        .status("CANCELED")
                        .isRescheduled(medicalAppointment.isRescheduled())
                        .build();

            } else {
                if (status.equals("SCHEDULED")) {
                    checkedAppointmentDate = medicalAppointmentDate;
                    checkAvailability(checkedDoctor, checkedPatient, checkedAppointmentDate, entity);
                }
                checkedAppointmentDate = entity.getAppointmentDate();

            }

        return MedicalAppointment.builder()
                .id(medicalAppointmentId)
                .createDate(checkedCreateDate)
                .updateDate(checkedUpdateDate)
                .appointmentDate(checkedAppointmentDate)
                .patient(checkedPatient)
                .phoneNumber(medicalAppointment.getPhoneNumber())
                .doctor(checkedDoctor)
                .medicalSpecialty(checkedMedicalSpeciality)
                .status(status)
                .isRescheduled(medicalAppointment.isRescheduled())
                .build();
    }




    public MedicalAppointment validadeCreateMedicalAppointment(MedicalAppointment medicalAppointment) {

        String status = medicalAppointment.getStatus();

        LocalDateTime createDate = LocalDateTime.now();
        String checkedPatient = isValidPatientName(medicalAppointment.getPatient());
        String checkedDoctor = isValidDoctorName(medicalAppointment.getDoctor());
        String checkedMedicalSpecialty = isValidDoctorMedicalSpecialty(checkedDoctor, medicalAppointment.getMedicalSpecialty());
        String checkedAppointmentDate = isValidAppointmentDate(medicalAppointment.getAppointmentDate());

        List<MedicalAppointmentEntity> medicalAppointmentEntityList = findAllMedicalAppointments();
        if (!medicalAppointmentEntityList.isEmpty()) {
            medicalAppointmentEntityList.forEach(entity -> {
                checkAvailability(checkedDoctor, checkedPatient, checkedAppointmentDate, entity);
            });
        }

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

    public void checkAvailability(String checkedDoctor, String checkedPatient, String validAppointmentDate, MedicalAppointmentEntity entity) {


        String entityDoctorName = entity.getDoctor();
        String entityPatientName = entity.getPatient();

        String entityStatus = Optional.of(entity)
                .map(MedicalAppointmentEntity::getStatus)
                .orElse(SCHEDULED);

        String entityAppointmentDate = entity.getAppointmentDate();


        String formattedEntityAppointmentDate = entityAppointmentDate.substring(11, 16);
        String formattedValidAppointmentDate = validAppointmentDate.substring(11, 16);


        LocalDateTime localDateTimeAppointmentDate = LocalDateTime.parse(entityAppointmentDate, formatter);
        LocalDateTime localDateTimeValidAppointmentDate = LocalDateTime.parse(validAppointmentDate, formatter);

        if (entityStatus.equals(SCHEDULED)) {
            if (entityDoctorName.equalsIgnoreCase(checkedDoctor) && entityPatientName.equalsIgnoreCase(checkedPatient)) {
                if (entityAppointmentDate.equals(validAppointmentDate)) {
                    throw new IllegalArgumentException("Patient already has an appointment with this doctor at: " + validAppointmentDate);

                } else {

                    if (!formattedEntityAppointmentDate.equals(formattedValidAppointmentDate)) {
                        throw new IllegalArgumentException("Patient already has an appointment with this doctor at the same day at: " + formattedEntityAppointmentDate);
                    }
                    if (localDateTimeValidAppointmentDate.isBefore(localDateTimeAppointmentDate.plusDays(30))) {
                        throw new IllegalArgumentException("Patient already has an appointment with this doctor within 30 days.");
                    }

                }
            }

        } else if (entityDoctorName.equalsIgnoreCase(checkedDoctor) && entityAppointmentDate.equals(validAppointmentDate)) {
            throw new IllegalArgumentException("Doctor is not available at: " + validAppointmentDate);
        } else if (entityPatientName.equalsIgnoreCase(checkedPatient) && entityAppointmentDate.equals(validAppointmentDate)) {
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


