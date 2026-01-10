package fiap.medicalappointmentsservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="tb_users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String medicalSpecialty;
    private String password;
    private String username;
    private String role;
}