package fiap.medicalappointmentsservice.infrastructure.persistence.repository;

import fiap.medicalappointmentsservice.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.name = :name AND u.role = :role")
    Optional<UserEntity> findByNameAndRole(@Param("name") String name, @Param("role") String role);

    @Query("SELECT u FROM UserEntity u WHERE u.name = :name AND u.medicalSpecialty = :medicalSpecialty")
    Optional<UserEntity> findByNameAndMedicalSpecialty(@Param("name") String name, @Param("medicalSpecialty") String medicalSpecialty);

    Optional<UserEntity> findByUsername(String username);
}
