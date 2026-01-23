package fiap.medicalappointmentsservice.domain.port.in;

import fiap.medicalappointmentsservice.infrastructure.persistence.entity.MedicalAppointmentEntity;
import java.util.List;

public interface MedicalAppointmentGraphQLInterface {

     List<MedicalAppointmentEntity> allAppointmentsForPatient(String patient);

     List<MedicalAppointmentEntity> futureAppointmentsForPatient(String patient) ;

}
