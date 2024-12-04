package app.finance.api.dto;

import lombok.Data;

// CategorySummary for the category summary
@Data // Lombok annotation to generate getter and setter methods
public class CategorySummary {
    private int id;
    private double value;
    private String label;

    // Constructor
    public CategorySummary(int id, double value, String label) {
        this.id = id;
        this.value = value;
        this.label = label;
    }
}