package app.finance.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name="user")
@Data
public class UserModel {

    @Id
    private int uid;

    private String name;

    @Column(unique = true)
    private String email;
    
    private String password;
    
    private String status;
}