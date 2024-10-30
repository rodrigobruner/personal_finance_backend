package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "account")
@Data
public class AccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private UserModel user;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "accountType_id", nullable = false)
    private AccountTypeModel accountType;

    @Column(name = "initialAmount")
    private double initialAmount;

    @Column(name = "updatedAmount")
    private double updatedAmount;

    @Column(name = "createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Active;
}