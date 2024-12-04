package app.finance.api.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

// CategoryModel class
@Entity
@Table(name = "category")
@Data
public class CategoryModel {
    //Category id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //User id
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private UserModel user;

    //Category name
    @Column(name = "name", nullable = false)
    private String name;

    //Category create date
    @Column(name = "createAt", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    //Category type
    @Enumerated(EnumType.STRING)
    @Column(name = "categoryType", nullable = false)
    private CategoryType categoryType = CategoryType.Expense;

    //Category status
    @PrePersist
    protected void onCreate() {
        if (createAt == null) {
            createAt = new Date();
        }
    }
}