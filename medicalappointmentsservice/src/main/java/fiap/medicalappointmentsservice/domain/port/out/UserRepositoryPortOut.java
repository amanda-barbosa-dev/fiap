package fiap.medicalappointmentsservice.domain.port.out;

import fiap.medicalappointmentsservice.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepositoryPortOut {

        Optional<UserEntity> findByNameAndRole(String name, String role);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByNameAndMedicalSpecialty(String name, String medicalSpecialty);
}
