package fiap.medicalappointmentsservice.infrastructure.persistence.repository;

import fiap.medicalappointmentsservice.domain.port.out.UserRepositoryPortOut;
import fiap.medicalappointmentsservice.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository implements UserRepositoryPortOut {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<UserEntity> findByNameAndRole(String name, String role) {
        return jpaUserRepository.findByNameAndRole(name.toUpperCase(), role.toUpperCase());
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username.toUpperCase());
    }


    @Override
    public Optional<UserEntity> findByNameAndMedicalSpecialty(@Param("name") String name, @Param("medicalSpecialty") String medicalSpecialty){
        return jpaUserRepository.findByNameAndMedicalSpecialty(name.toUpperCase(), medicalSpecialty.toUpperCase());
    };
}
