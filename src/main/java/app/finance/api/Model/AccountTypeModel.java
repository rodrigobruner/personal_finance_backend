package app.finance.api.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "accountType")
@Data
public class AccountTypeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "locale")
    private String locale;

    @JsonCreator
    public AccountTypeModel(@JsonProperty("id") int id) {
        this.id = id;
    }

    public AccountTypeModel() {

    }
}