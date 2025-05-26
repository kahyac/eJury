package amu.jury_m1.domain;

public class KnowledgeBlock {
    private final String code;
    private final String label;
    private final int semester;

    public KnowledgeBlock(String code, String label, int semester) {
        this.code = code;
        this.label = label;
        this.semester = semester;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }
    public int getSemester() { return semester; }
}