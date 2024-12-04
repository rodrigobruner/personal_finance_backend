package app.finance.api.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "accountType")
@Data
public class AccountTypeModel {
    //Account type id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Account type
    @Column(name = "type")
    private String type;

    //Account type locale
    @Column(name = "locale")
    private String locale;

    //Account type create date
    @JsonCreator // This is a Jackson annotation that helps Jackson to understand that this constructor is to be used to create objects from JSON
    public AccountTypeModel(@JsonProperty("id") int id) {
        this.id = id;
    }

    public AccountTypeModel() {

    }
}