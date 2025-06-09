package amu.jury_m1.service.academicYear;

import amu.jury_m1.model.result.Mention;

/**
 * Business rule for mapping an average score to a final mention.
 */
public class MentionEvaluator {

    public Mention evaluate(double average) {
        if (average >= 18) return Mention.EXCELLENT;
        else if (average >= 16) return Mention.VERY_GOOD;
        else if (average >= 14) return Mention.GOOD;
        else if (average >= 12) return Mention.PASS;
        else return Mention.NONE;
    }
}
