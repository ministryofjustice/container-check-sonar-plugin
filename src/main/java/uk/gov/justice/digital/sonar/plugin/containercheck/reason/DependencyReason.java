package uk.gov.justice.digital.sonar.plugin.containercheck.reason;

import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.sensor.SensorContext;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetric;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Analysis;

public interface DependencyReason {


    /**
     * @return InputComponent with includes the dependency
     */
    InputComponent getInputComponent();

    /**
     * @return the metrics
     */
    ContainerCheckMetric getMetrics();

    /**
     * Add the issues / vulnerabilities found in the Analysis object to the SonarQube context.
     *
     * @param context
     *          The SonarQube context
     * @param analysis
     *          The result of Sonar analysis
     */
    void addIssues(SensorContext context, Analysis analysis);

}
