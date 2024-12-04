package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

// TransactionModel class
@Entity
@Table(name = "transaction")
@Data
public class TransactionModel {
    //Transaction id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Account id
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountModel account;

    //Category id
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    //  Transaction type
    @Column(name = "value")
    private double value;

    //Transaction notes
    @Column(name = "notes")
    private String notes;

    //Transaction date
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    //Transaction create date
    @Column(name = "createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
}