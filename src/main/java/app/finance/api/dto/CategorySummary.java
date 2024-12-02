package app.finance.api.dto;

import lombok.Data;

@Data
public class CategorySummary {
    private int id;
    private double value;
    private String label;

    public CategorySummary(int id, double value, String label) {
        this.id = id;
        this.value = value;
        this.label = label;
    }
}