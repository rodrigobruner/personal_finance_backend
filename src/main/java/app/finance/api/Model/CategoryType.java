package app.finance.api.Model;

public enum CategoryType {
    Income,
    Expense;

    public static CategoryType fromString(String category) {
        if (category != null) {
            String normalizedCategory = category.trim().toUpperCase();
            for (CategoryType type : CategoryType.values()) {
                if (type.name().equalsIgnoreCase(normalizedCategory)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Invalid category type: " + category);
    }
}