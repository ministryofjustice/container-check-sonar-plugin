package uk.gov.justice.digital.sonar.plugin.containercheck.rule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.LANGUAGE_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.REPOSITORY_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.RULE_KEY;

import org.junit.jupiter.api.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.Context;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile;

class NeutralProfileTest {

    @Test
    void testProfile() {
        final Context context = mock(Context.class);
        final NewBuiltInQualityProfile profile = mock(NewBuiltInQualityProfile.class);
        when(context.createBuiltInQualityProfile("Neutral", LANGUAGE_KEY)).thenReturn(profile);

        // Act
        new NeutralProfile().define(context);

        // Assert
        verify(context).createBuiltInQualityProfile("Neutral", LANGUAGE_KEY);
        verify(profile).activateRule(REPOSITORY_KEY, RULE_KEY);
        verify(profile).done();
        verifyNoMoreInteractions(context, profile);
    }
}
