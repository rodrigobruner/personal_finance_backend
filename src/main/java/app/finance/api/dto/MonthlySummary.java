package app.finance.api.dto;

import app.finance.api.Model.CategoryType;
import lombok.Data;

// MonthlySummary for the monthly summary
@Data // Lombok annotation to generate getter and setter methods
public class MonthlySummary {
    private int month;
    private int year;
    private CategoryType categoryType;
    private double totalValue;

    // Constructor
    public MonthlySummary(int month, int year, CategoryType categoryType, double totalValue) {
        this.month = month;
        this.year = year;
        this.categoryType = categoryType;
        this.totalValue = totalValue;
    }
}