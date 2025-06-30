package amu.eJury.service.Impl;

import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.service.impl.DefaultStatusPropagationRuleService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
@RequiredArgsConstructor
class DefaultStatusPropagationRuleServiceTest {

    private final DefaultStatusPropagationRuleService rule = new DefaultStatusPropagationRuleService();

    @Test void abi_preempts_everything() {
        assertThat(rule.propagate(ExceptionalStatus.ABI, ExceptionalStatus.ABJ))
                .isEqualTo(ExceptionalStatus.ABI);
        assertThat(rule.propagate(ExceptionalStatus.AR,  ExceptionalStatus.ABI))
                .isEqualTo(ExceptionalStatus.ABI);
    }

    @Test void abj_preempts_ar() {
        assertThat(rule.propagate(ExceptionalStatus.ABJ, ExceptionalStatus.AR))
                .isEqualTo(ExceptionalStatus.ABJ);
    }

    @Test void none_if_both_none() {
        assertThat(rule.propagate(ExceptionalStatus.NONE, ExceptionalStatus.NONE))
                .isEqualTo(ExceptionalStatus.NONE);
    }
}

