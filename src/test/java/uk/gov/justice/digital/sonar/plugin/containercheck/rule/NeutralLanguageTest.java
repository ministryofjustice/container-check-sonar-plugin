package uk.gov.justice.digital.sonar.plugin.containercheck.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class NeutralLanguageTest {
    @Test
    void getFileSuffixes() {
        assertEquals(0, new NeutralLanguage().getFileSuffixes().length);
    }

}
