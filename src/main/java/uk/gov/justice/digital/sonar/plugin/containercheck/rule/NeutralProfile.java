package uk.gov.justice.digital.sonar.plugin.containercheck.rule;

import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.LANGUAGE_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.REPOSITORY_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.RULE_KEY;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class NeutralProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(final Context context) {
        final NewBuiltInQualityProfile builtInQualityProfile = context.createBuiltInQualityProfile("Neutral", LANGUAGE_KEY);
        builtInQualityProfile.activateRule(REPOSITORY_KEY, RULE_KEY);
        builtInQualityProfile.done();
    }
}
