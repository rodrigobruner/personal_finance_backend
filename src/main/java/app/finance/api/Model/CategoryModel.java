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
    @JoinColumn(name = "user_uid", nullable = false)
    private UserModel user;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "createAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoryType", nullable = false)
    private CategoryType categoryType = CategoryType.Expense;
}