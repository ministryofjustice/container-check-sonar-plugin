package uk.gov.justice.digital.sonar.plugin.containercheck.reason;

import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.REPOSITORY_KEY;

import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewExternalIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetric;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Analysis;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Vulnerability;

public class ContainerImageDependencyReason implements DependencyReason {

    private final ContainerCheckMetric containerCheckMetric;

    private final InputComponent inputFile;

    private static final Logger LOGGER = Loggers.get(ContainerImageDependencyReason.class);

    public ContainerImageDependencyReason(final InputFile inputFile) {
        this.containerCheckMetric = new ContainerCheckMetric(inputFile);
        this.inputFile = inputFile;
    }

    /**
     * returns Dockerfile
     */
    @Override
    public InputComponent getInputComponent() {
        return inputFile;
    }

    @Override
    public ContainerCheckMetric getMetrics() {
        return containerCheckMetric;
    }

    public void addIssues(final SensorContext context, final Analysis analysis) {
        if (analysis.getVulnerabilities() == null || analysis.getVulnerabilities().isEmpty()) {
            LOGGER.info("Analyse doesn't report any vulnerabilities for dependency {}", analysis.getTarget());
            return;
        }

        containerCheckMetric.increaseVulnerabilityCount(analysis.getVulnerabilities().size());
        for (final Vulnerability vulnerability : analysis.getVulnerabilities()) {
            addIssue(context, vulnerability);
        }
        containerCheckMetric.saveMeasures(context);
    }

    void addIssue(final SensorContext context, final Vulnerability vulnerability) {
        final NewExternalIssue sonarIssue  = context.newExternalIssue();

        final NewIssueLocation location = sonarIssue.newLocation()
                .on(inputFile)
                .message(vulnerability.toSonarMessage());

        sonarIssue
            .at(location)
            .type(RuleType.VULNERABILITY)
            .ruleId(vulnerability.getId())
            .engineId(REPOSITORY_KEY)
            .severity(vulnerability.getSeverity().asSonar())
            .save();
        containerCheckMetric.incrementCount(vulnerability.getSeverity().asSonar());
    }

}
