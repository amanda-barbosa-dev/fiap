package fiap.medicalappointmentsservice.application.service.mapper;

import fiap.medicalappointmentsservice.domain.dto.CreateAppointmentDto;
import fiap.medicalappointmentsservice.domain.dto.UpdateAppointmentDto;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus.SCHEDULED;

@Component
@Data
public class MedicalAppointmentMapper {


    public static MedicalAppointment mapCreateMedicalAppointmentDtoToMedicalAppointment(CreateAppointmentDto createAppointmentDto) {
        Long validId = Optional.of(createAppointmentDto).map(CreateAppointmentDto::getId).orElse(null);

        return MedicalAppointment.builder()
                .id(validId)
                .createDate(null)
                .updateDate(null)
                .patient(createAppointmentDto.getPatient())
                .phoneNumber(createAppointmentDto.getPhoneNumber())
                .doctor(createAppointmentDto.getDoctor())
                .medicalSpecialty(createAppointmentDto.getMedicalSpecialty())
                .appointmentDate(createAppointmentDto.getAppointmentDate())
                .status(String.valueOf(SCHEDULED))
                .build();
    }

    public static MedicalAppointment mapUpdateMedicalAppointmentDtoToMedicalAppointment(Long id, UpdateAppointmentDto updateAppointmentDto, AppointmentStatus appointmentStatus) {
        String date = Optional.of(updateAppointmentDto.getAppointmentDate()).orElse(null);
        boolean isRescheduled;
        String status;
        status = String.valueOf(Objects.requireNonNullElse(appointmentStatus, SCHEDULED));

        isRescheduled = status.equals(String.valueOf(SCHEDULED));

        return MedicalAppointment.builder()
                .id(id)
                .appointmentDate(date)
                .status(status)
                .isRescheduled(isRescheduled)
                .build();
    }

    public static MedicalAppointmentEntity mapValidMedicalAppointmentToMedicalAppointmentEntity(MedicalAppointment medicalAppointment) {
        return new MedicalAppointmentEntity(medicalAppointment);
    }

    public static MedicalAppointment mapMedicalAppointmentEntityToMedicalAppointment(MedicalAppointmentEntity entity) {


        return MedicalAppointment.builder()
                .id(entity.getId())
                .patient(entity.getPatient())
                .phoneNumber(entity.getPhoneNumber())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .doctor(entity.getDoctor())
                .medicalSpecialty(entity.getMedicalSpecialty())
                .status(entity.getStatus())
                .appointmentDate(entity.getAppointmentDate())
                .build();


    }
}
