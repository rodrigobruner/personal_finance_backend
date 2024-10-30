package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transaction")
@Data
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountModel account;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @Column(name = "value")
    private double value;

    @Column(name = "notes")
    private String notes;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
}