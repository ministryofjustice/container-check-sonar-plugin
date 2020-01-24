package uk.gov.justice.digital.sonar.plugin.containercheck.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.CRITICAL_SEVERITY_VULNS_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.HIGH_SEVERITY_VULNS_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.LOW_SEVERITY_VULNS_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.MEDIUM_SEVERITY_VULNS_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.TOTAL_VULNERABILITIES_KEY;

import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.measure.Measure;

@ExtendWith(MockitoExtension.class)
class ContainerCheckMetricTest {

    @Mock
    private InputComponent inputComponent;

    @InjectMocks
    private ContainerCheckMetric containerCheckMetric;

    @DisplayName("Increment the CRITICAL counter. Trivy supplies no BLOCKER severity.")
    @Test
    void testIncrementCriticalAndBlocker() {
        containerCheckMetric.incrementCount(Severity.CRITICAL);
        containerCheckMetric.incrementCount(Severity.CRITICAL);
        containerCheckMetric.incrementCount(Severity.BLOCKER);
        assertEquals(3, containerCheckMetric.getCriticalIssuesCount());
    }

    @DisplayName("Increment the MAJOR counter")
    @Test
    void testIncrementMajor() {
        containerCheckMetric.incrementCount(Severity.MAJOR);
        assertEquals(1, containerCheckMetric.getMajorIssuesCount());
    }

    @DisplayName("Increment the MINOR counter")
    @Test
    void testIncrementMinor() {
        containerCheckMetric.incrementCount(Severity.MINOR);
        assertEquals(1, containerCheckMetric.getMinorIssuesCount());
    }

    @DisplayName("Increment the INFO counter")
    @Test
    void testIncrementInfo() {
        containerCheckMetric.incrementCount(Severity.INFO);
        assertEquals(1, containerCheckMetric.getInfoIssuesCount());
    }

    @DisplayName("Increment the general vulnerability counter")
    @Test
    void testIncrementVulnCount() {
        containerCheckMetric.increaseVulnerabilityCount(198);
        assertEquals(198, containerCheckMetric.getVulnerabilityCount());
    }

    @DisplayName("Tests the save operation.")
    @Test
    void testSaveMeasures() {
        final SensorContextTester sensorContext = SensorContextTester.create(new File("."));
        when(inputComponent.key()).thenReturn("IN-KEY");
        containerCheckMetric.incrementCount(Severity.CRITICAL);
        containerCheckMetric.incrementCount(Severity.MAJOR);
        containerCheckMetric.incrementCount(Severity.CRITICAL);
        containerCheckMetric.incrementCount(Severity.BLOCKER);
        containerCheckMetric.incrementCount(Severity.MINOR);
        containerCheckMetric.incrementCount(Severity.MINOR);
        containerCheckMetric.incrementCount(Severity.INFO);
        containerCheckMetric.incrementCount(Severity.INFO);
        containerCheckMetric.incrementCount(Severity.INFO);
        containerCheckMetric.increaseVulnerabilityCount(9);

        // Act
        containerCheckMetric.saveMeasures(sensorContext);
        //
        final Map<String, Integer> expectedMap = Map.of(CRITICAL_SEVERITY_VULNS_KEY, Integer.valueOf(3), // 2 CRITICAL + 1 BLOCKER
                                                        HIGH_SEVERITY_VULNS_KEY, Integer.valueOf(1), // 1 MAJOR
                                                        MEDIUM_SEVERITY_VULNS_KEY, Integer.valueOf(2), // 2 MINOR
                                                        LOW_SEVERITY_VULNS_KEY, Integer.valueOf(3), // 3 INFO
                                                        TOTAL_VULNERABILITIES_KEY, Integer.valueOf(9));
        for (Map.Entry<String, Integer> entry : expectedMap.entrySet()) {
            final Measure<Integer> measure = sensorContext.measure("IN-KEY", entry.getKey());
            assertEquals(entry.getValue().intValue(), measure.value().intValue(), entry.getKey());
        }
    }

    @DisplayName("Increment the general vulnerability counter")
    @Test
    void testToString() {
        assertThat(containerCheckMetric.toString()).contains("ContainerCheckMetric");
    }
}
