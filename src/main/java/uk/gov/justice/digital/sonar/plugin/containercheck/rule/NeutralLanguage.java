package uk.gov.justice.digital.sonar.plugin.containercheck.rule;

import org.sonar.api.resources.AbstractLanguage;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants;

/**
 * In order for a rule repository to work properly, the rules created in the repository
 * must be associated with a language. This is a workaround so that rules that apply to
 * third-party components where the language is not known (or irrelevant) can be used to
 * flag those components as vulnerable.
 *
 * This class simply creates a new 'language' called neutral.
 */
public class NeutralLanguage extends AbstractLanguage {

    public NeutralLanguage() {
        super(ContainerCheckConstants.LANGUAGE_KEY, "Neutral");
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[0];
    }

}
