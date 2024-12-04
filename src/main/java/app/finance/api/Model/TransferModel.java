package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

// TransferModel class
@Entity
@Table(name = "transfer")
@Data
public class TransferModel {
    //Transfer id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //From account id
    @ManyToOne
    @JoinColumn(name = "fromIdAccount", nullable = false)
    private AccountModel fromAccount;

    //To account id
    @ManyToOne
    @JoinColumn(name = "toIdAcoount", nullable = false)
    private AccountModel toAccount;

    //Transfer value
    @Column(name = "value")
    private double value;

    //Transfer notes
    @Column(name = "notes")
    private String notes;

    //Transfer date
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    //Transfer create date
    @Column(name = "createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;    
}