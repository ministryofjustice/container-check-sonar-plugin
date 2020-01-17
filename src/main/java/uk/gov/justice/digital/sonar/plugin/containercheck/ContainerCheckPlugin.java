package uk.gov.justice.digital.sonar.plugin.containercheck;

import java.util.Arrays;

import org.sonar.api.Plugin;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckMetrics;
import uk.gov.justice.digital.sonar.plugin.containercheck.page.ContainerCheckReportPage;
import uk.gov.justice.digital.sonar.plugin.containercheck.rule.KnownVulnerabilityRuleDefinition;
import uk.gov.justice.digital.sonar.plugin.containercheck.rule.NeutralLanguage;
import uk.gov.justice.digital.sonar.plugin.containercheck.rule.NeutralProfile;

public final class ContainerCheckPlugin implements Plugin {

    @Override
    public void define(final Context context) {
        context.addExtensions(Arrays.asList(
            ContainerCheckSensor.class,
            ContainerCheckMetrics.class,
            NeutralProfile.class,
            NeutralLanguage.class,
            KnownVulnerabilityRuleDefinition.class,
            ContainerCheckReportPage.class));
        context.addExtensions(ContainerCheckConfiguration.getPropertyDefinitions());
    }

}
