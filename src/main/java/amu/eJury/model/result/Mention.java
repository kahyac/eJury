package amu.eJury.model.result;

public enum Mention {
    PASS("Passable"),
    FAIRLY_GOOD("Assez bien"),
    GOOD("Bien"),
    VERY_GOOD("Très bien"),
    EXCELLENT("Excellent");

    private final String label;

    Mention(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}