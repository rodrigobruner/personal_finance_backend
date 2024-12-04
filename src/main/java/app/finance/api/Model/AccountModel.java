package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

// AccountModel class
@Entity // This will let Java know that this class is an entity that we are going to map to a database table
@Table(name = "account") // This is for the actual name of the database table we are mapping to the class
@Data // Lombok annotation to create all the getters, setters, equals, hash, and toString methods for us
public class AccountModel {

    //Account id
    // This is the primary key of the account table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This will auto increment the id value
    private int id;

    //User id
    @ManyToOne // This is for the many side of the one to many relationship between the account and user table
    @JoinColumn(name = "uid", nullable = false) // This is for the foreign key relationship between the account and user table
    private UserModel user;

    //Account name
    @Column(name = "name")
    private String name;

    //Account type
    @ManyToOne
    @JoinColumn(name = "accountType_id", nullable = false)
    private AccountTypeModel accountType;

    //Account initial amount
    @Column(name = "initialAmount")
    private double initialAmount;

    //Account updated amount
    @Column(name = "updatedAmount")
    private double updatedAmount;

    //Account create date
    @Column(name = "createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    //Account update date
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Active;
}