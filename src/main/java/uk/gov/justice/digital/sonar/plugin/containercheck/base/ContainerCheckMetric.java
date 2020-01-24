package uk.gov.justice.digital.sonar.plugin.containercheck.base;

import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.CRITICAL_SEVERITY_VULNS;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.HIGH_SEVERITY_VULNS;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.LOW_SEVERITY_VULNS;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.MEDIUM_SEVERITY_VULNS;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics.TOTAL_VULNERABILITIES;

import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Records and also saves the metrics derived from the scan of the report file.
 */
public class ContainerCheckMetric {

    private static final Logger LOGGER = Loggers.get(ContainerCheckMetric.class);
    private final InputComponent inputcomponent;

    private int vulnerabilityCount;
    private int criticalIssuesCount;
    private int majorIssuesCount;
    private int minorIssuesCount;
    private int infoIssuesCount;

    public ContainerCheckMetric(final InputComponent inputComponent) {
        this.inputcomponent = inputComponent;
    }

    public void incrementCount(final Severity severity) {
        switch (severity) {
            case BLOCKER:
            case CRITICAL:
                this.criticalIssuesCount++;
                break;
            case MAJOR:
                this.majorIssuesCount++;
                break;
            case MINOR:
                this.minorIssuesCount++;
                break;
            case INFO:
                this.infoIssuesCount++;
                break;
            default:
                LOGGER.warn("Unknown severity {}", severity);
                break;
        }
    }

    public void increaseVulnerabilityCount(final int amount) {
        this.vulnerabilityCount += amount;
    }

    public void saveMeasures(final SensorContext context) {
        LOGGER.debug("Save measures on {}", inputcomponent);

        context.<Integer>newMeasure().forMetric(CRITICAL_SEVERITY_VULNS).on(inputcomponent)
            .withValue(criticalIssuesCount)
            .save();

        context.<Integer>newMeasure().forMetric(HIGH_SEVERITY_VULNS).on(inputcomponent)
            .withValue(majorIssuesCount)
            .save();

        context.<Integer>newMeasure().forMetric(MEDIUM_SEVERITY_VULNS).on(inputcomponent)
            .withValue(minorIssuesCount)
            .save();

        context.<Integer>newMeasure().forMetric(LOW_SEVERITY_VULNS).on(inputcomponent)
            .withValue(infoIssuesCount)
            .save();

        context.<Integer>newMeasure().forMetric(TOTAL_VULNERABILITIES).on(inputcomponent)
            .withValue(vulnerabilityCount)
            .save();
    }

    public int getVulnerabilityCount() {
        return vulnerabilityCount;
    }

    public int getCriticalIssuesCount() {
        return criticalIssuesCount;
    }

    public int getMajorIssuesCount() {
        return majorIssuesCount;
    }

    public int getMinorIssuesCount() {
        return minorIssuesCount;
    }

    public int getInfoIssuesCount() {
        return infoIssuesCount;
    }

    @Override
    public String toString() {
        return "ContainerCheckMetric [" +
            ", vulnerabilityCount=" + vulnerabilityCount +
            ", criticalIssuesCount=" + criticalIssuesCount +
            ", majorIssuesCount=" + majorIssuesCount +
            ", minorIssuesCount=" + minorIssuesCount +
            ", infoIssuesCount=" + infoIssuesCount +
            ']';
    }
}
