package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "category")
@Data
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private UserModel user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "createAt", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoryType", nullable = false)
    private CategoryType categoryType = CategoryType.Expense;

    @PrePersist
    protected void onCreate() {
        if (createAt == null) {
            createAt = new Date();
        }
    }
}