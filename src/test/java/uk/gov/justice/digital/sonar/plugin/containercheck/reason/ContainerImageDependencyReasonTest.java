package uk.gov.justice.digital.sonar.plugin.containercheck.reason;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.REPOSITORY_KEY;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewExternalIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rules.RuleType;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetric;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Analysis;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Vulnerability;


class ContainerImageDependencyReasonTest {

    private ContainerImageDependencyReason imageDependencyReason;

    @Mock
    private InputFile inputComponent;

    @Mock
    private SensorContext sensorContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageDependencyReason = new ContainerImageDependencyReason(inputComponent);
        assertSame(inputComponent, imageDependencyReason.getInputComponent());
    }

    @DisplayName("Add single vulnerability")
    @Test
    void testAddIssue() {
        final NewExternalIssue sonarIssue = mock(NewExternalIssue.class);
        final NewIssueLocation issueLocation = mock(NewIssueLocation.class);

        mockControlForAddIssue(sonarIssue, issueLocation, "CVE-2011", Severity.MAJOR);
        when(sensorContext.newExternalIssue()).thenReturn(sonarIssue);

        final Vulnerability vulnerability = new Vulnerability("CVE-2011", "bash", "1.0", null, "CVE-TITLE",
                                                                "Description", "HIGH", Collections.emptyList());
        // Act
        imageDependencyReason.addIssue(sensorContext, vulnerability);

        // Assert
        verifyForSingleIssueSave(sonarIssue, issueLocation, "CVE-2011", Severity.MAJOR);
        verifySensorContext(1);

        // Only 1 MAJOR issue, showing
        assertEquals(1, imageDependencyReason.getMetrics().getMajorIssuesCount());
        assertEquals(0, imageDependencyReason.getMetrics().getCriticalIssuesCount());
        assertEquals(0, imageDependencyReason.getMetrics().getVulnerabilityCount());
    }

    @DisplayName("Analysis object but null vulnerabilities")
    @Test
    void testAddIssuesNullVulns() {

        // Act
        imageDependencyReason.addIssues(sensorContext, new Analysis("target", null));

        // Assert
        assertEquals(0, imageDependencyReason.getMetrics().getVulnerabilityCount());
        verifySensorContext(0);
    }

    @DisplayName("Analysis object but no vulnerabilities")
    @Test
    void testAddIssuesEmptyVulns() {

        // Act
        imageDependencyReason.addIssues(sensorContext, new Analysis("target", Collections.emptyList()));

        // Assert
        assertEquals(0, imageDependencyReason.getMetrics().getVulnerabilityCount());
        verifySensorContext(0);
    }

    @DisplayName("Add multiple issues")
    @Test
    void testAddIssues() {
        final ContainerCheckMetric containerCheckMetric = mock(ContainerCheckMetric.class);
        ReflectionTestUtils.setField(imageDependencyReason, "containerCheckMetric", containerCheckMetric);

        final NewExternalIssue sonarIssue1 = mock(NewExternalIssue.class);
        final NewIssueLocation issueLocation1 = mock(NewIssueLocation.class);
        mockControlForAddIssue(sonarIssue1, issueLocation1, "CVE-2011", Severity.MINOR);

        final NewExternalIssue sonarIssue2 = mock(NewExternalIssue.class);
        final NewIssueLocation issueLocation2 = mock(NewIssueLocation.class);
        mockControlForAddIssue(sonarIssue2, issueLocation2, "CVE-2012", Severity.MAJOR);

        when(sensorContext.newExternalIssue()).thenReturn(sonarIssue1, sonarIssue2);

        final Vulnerability vulnerability1 = new Vulnerability("CVE-2011", "bash", "1.0", null, "CVE-2011-TITLE",
            "Description", "MEDIUM", Collections.emptyList());
        final Vulnerability vulnerability2 = new Vulnerability("CVE-2012", "openssl", "2.0", null, "CVE-2012-TITLE",
            "Description", "UNKNOWN", Collections.emptyList());

        // Act
        imageDependencyReason.addIssues(sensorContext, new Analysis("target", Arrays.asList(vulnerability1, vulnerability2)));

        // Assert
        verifyForSingleIssueSave(sonarIssue1, issueLocation1, "CVE-2011", Severity.MINOR);
        verifyForSingleIssueSave(sonarIssue2, issueLocation2, "CVE-2012", Severity.MAJOR);
        verifySensorContext(2);

        verify(containerCheckMetric).increaseVulnerabilityCount(2);
        verify(containerCheckMetric, times(1)).incrementCount(Severity.MAJOR);
        verify(containerCheckMetric, times(1)).incrementCount(Severity.MINOR);
        verify(containerCheckMetric).saveMeasures(sensorContext);
        verifyNoMoreInteractions(containerCheckMetric);
    }

    private void mockControlForAddIssue(final NewExternalIssue sonarIssue,
                                        final NewIssueLocation issueLocation,
                                        final String expectedId,
                                        final Severity expectedSeverity) {

        when(sonarIssue.newLocation()).thenReturn(issueLocation);
        when(issueLocation.on(inputComponent)).thenReturn(issueLocation);
        when(issueLocation.message(contains(expectedId))).thenReturn(issueLocation);

        when(sonarIssue.at(issueLocation)).thenReturn(sonarIssue);
        when(sonarIssue.type(RuleType.VULNERABILITY)).thenReturn(sonarIssue);
        when(sonarIssue.ruleId(eq(expectedId))).thenReturn(sonarIssue);
        when(sonarIssue.engineId(REPOSITORY_KEY)).thenReturn(sonarIssue);
        when(sonarIssue.severity(expectedSeverity)).thenReturn(sonarIssue);
    }

    private void verifyForSingleIssueSave(final NewExternalIssue sonarIssue,
                                        final NewIssueLocation issueLocation,
                                        final String expectedId,
                                        final org.sonar.api.batch.rule.Severity expectedSeverity) {
        verify(sonarIssue).save();
        verify(sonarIssue).newLocation();
        verify(issueLocation).on(inputComponent);
        verify(issueLocation).message(contains(expectedId));
        verify(sonarIssue).at(issueLocation);
        verify(sonarIssue).type(RuleType.VULNERABILITY);
        verify(sonarIssue).ruleId(expectedId);
        verify(sonarIssue).engineId(REPOSITORY_KEY);
        verify(sonarIssue).severity(expectedSeverity);

        verifyNoMoreInteractions(sonarIssue, issueLocation);
    }

    private void verifySensorContext(final int invocations) {
        verify(sensorContext, times(invocations)).newExternalIssue();
        verifyNoMoreInteractions(sensorContext);
    }
}
