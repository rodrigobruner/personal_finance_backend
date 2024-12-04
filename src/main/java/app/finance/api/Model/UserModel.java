package app.finance.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// UserModel class
@Entity
@Table(name = "user")
@Data
public class UserModel {
    //User id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private int uid;

    //User name
    @Column(nullable = false)
    private String name;

    //User email
    @Column(unique = true, nullable = false)
    private String email;

    //User password
    @Column(nullable = false)
    private String password;

    //User status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.Active;
}