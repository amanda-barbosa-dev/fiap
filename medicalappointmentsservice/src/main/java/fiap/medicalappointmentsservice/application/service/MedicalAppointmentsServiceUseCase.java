package fiap.medicalappointmentsservice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.medicalappointmentsservice.application.service.mapper.MedicalAppointmentMapper;
import fiap.medicalappointmentsservice.domain.dto.CreateAppointmentDto;
import fiap.medicalappointmentsservice.domain.dto.UpdateAppointmentDto;
import fiap.medicalappointmentsservice.domain.model.MedicalAppointment;
import fiap.medicalappointmentsservice.domain.port.in.SchedulerServicePortIn;
import fiap.medicalappointmentsservice.domain.port.out.EventProducerPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import fiap.medicalappointmentsservice.infrastructure.persistence.repository.MedicalAppointmentRepository;
import fiap.medicalappointmentsservice.shared.enuns.AppointmentStatus;
import fiap.medicalappointmentsservice.shared.validator.MedicalAppointmentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static fiap.medicalappointmentsservice.application.service.mapper.MedicalAppointmentMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalAppointmentsServiceUseCase implements SchedulerServicePortIn {

    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final MedicalAppointmentValidator medicalAppointmentValidator;
    private final EventProducerPortOut eventProducerPortOut;
    private final String TOPIC = "medical-appointments-topic";

    @Override
    public MedicalAppointment createMedicalAppointment(CreateAppointmentDto createAppointmentDto) {
        log.info("Service - createMedicalAppointment - request: {}", createAppointmentDto);

        MedicalAppointment medicalAppointmentRequest = mapCreateMedicalAppointmentDtoToMedicalAppointment(createAppointmentDto);

        MedicalAppointment validMedicalAppointment = medicalAppointmentValidator.validadeCreateMedicalAppointment(medicalAppointmentRequest);

        MedicalAppointmentEntity mappedMedicalAppointmentEntity = mapValidMedicalAppointmentToMedicalAppointmentEntity(validMedicalAppointment);

        try {
            log.info("Service - createMedicalAppointment - Saving medical appointment");
            medicalAppointmentRepository.save(mappedMedicalAppointmentEntity);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error saving medical appointment: " + e.getMessage());

        }

        MedicalAppointment medicalAppointmentResponse = mapMedicalAppointmentEntityToMedicalAppointment(mappedMedicalAppointmentEntity);

        log.info("Service - createMedicalAppointment - response: {}", medicalAppointmentResponse);

        String medicalAppointmentEventJson = mapMedicalAppointmentToJson(medicalAppointmentResponse);
        log.info("Service - updateMedicalAppointment - sending kafka evet: {}", medicalAppointmentEventJson);

        eventProducerPortOut.sendEvent(TOPIC, medicalAppointmentEventJson);


        return medicalAppointmentResponse;

    }


    @Override
    public MedicalAppointment updateMedicalAppointment(Long id, UpdateAppointmentDto updateAppointmentDto, AppointmentStatus appointmentStatus) {
        log.info("Service - updateMedicalAppointment - request: ID {}: {} - {}", id, updateAppointmentDto, appointmentStatus);

        MedicalAppointment medicalAppointmentRequest = mapUpdateMedicalAppointmentDtoToMedicalAppointment(id, updateAppointmentDto, appointmentStatus);
        MedicalAppointment validMedicalAppointment = medicalAppointmentValidator.validateUpdateMedicalAppointment(medicalAppointmentRequest);
        MedicalAppointmentEntity mappedMedicalAppointmentEntity;

        try {
            log.info("Service - updateMedicalAppointment - Updating medical appointment");

            if (validMedicalAppointment.isRescheduled()) {
                medicalAppointmentRepository.update(id,validMedicalAppointment.getStatus(), validMedicalAppointment.getAppointmentDate());
                mappedMedicalAppointmentEntity = mapValidMedicalAppointmentToMedicalAppointmentEntity(validMedicalAppointment);
                medicalAppointmentRepository.save(mappedMedicalAppointmentEntity);



            } else {
                medicalAppointmentRepository.update(id, validMedicalAppointment.getStatus(), validMedicalAppointment.getAppointmentDate());
                mappedMedicalAppointmentEntity = mapValidMedicalAppointmentToMedicalAppointmentEntity(validMedicalAppointment);
            }


        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating medical appointment: " + e.getMessage());

        }

        MedicalAppointment medicalAppointmentResponse = mapMedicalAppointmentEntityToMedicalAppointment(mappedMedicalAppointmentEntity);

        log.info("Service - updateMedicalAppointment - response: {}", medicalAppointmentResponse);

        String medicalAppointmentEventJson = mapMedicalAppointmentToJson(medicalAppointmentResponse);
        log.info("Service - updateMedicalAppointment - sending kafka evet: {}", medicalAppointmentEventJson);

        eventProducerPortOut.sendEvent(TOPIC, medicalAppointmentEventJson);

        return medicalAppointmentResponse;
    }


    public List<MedicalAppointment> allAppointmentsForPatient(String patient) {
        List<MedicalAppointmentEntity> appointmentEntitiesList = medicalAppointmentRepository.findByPatient(patient.toUpperCase());
        return appointmentEntitiesList.stream().map(MedicalAppointmentMapper::mapMedicalAppointmentEntityToMedicalAppointment).toList();
    }


    public List<MedicalAppointment> futureAppointmentsForPatient(String patient) {
        List<MedicalAppointmentEntity> appointmentEntitiesList = medicalAppointmentRepository.findFutureByPatient(patient.toUpperCase());
        return appointmentEntitiesList.stream().map(MedicalAppointmentMapper::mapMedicalAppointmentEntityToMedicalAppointment).toList();
    }

}
