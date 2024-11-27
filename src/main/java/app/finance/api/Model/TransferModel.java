package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transfer")
@Data
public class TransferModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "fromIdAccount", nullable = false)
    private AccountModel fromAccount;

    @ManyToOne
    @JoinColumn(name = "toIdAcoount", nullable = false)
    private AccountModel toAccount;

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



    public void setValue(String value) {
        this.value = Double.parseDouble(value.replace("$", ""));
    }
    
}