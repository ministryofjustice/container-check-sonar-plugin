package uk.gov.justice.digital.sonar.plugin.containercheck.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SeverityTest {

    @DisplayName("Tests mapping of severities. Strangely Sonar have implemented what looks like an enum as a static set of strings.")
    @Test
    void testMapSeverity() {

        assertEquals(org.sonar.api.rule.Severity.CRITICAL, Severity.CRITICAL.asSonar().toString());

        assertEquals(org.sonar.api.rule.Severity.MAJOR, Severity.UNKNOWN.asSonar().toString());
        assertEquals(org.sonar.api.rule.Severity.MAJOR, Severity.HIGH.asSonar().toString());

        assertEquals(org.sonar.api.rule.Severity.MINOR, Severity.MEDIUM.asSonar().toString());

        assertEquals(org.sonar.api.rule.Severity.INFO, Severity.LOW.asSonar().toString());
    }

}
