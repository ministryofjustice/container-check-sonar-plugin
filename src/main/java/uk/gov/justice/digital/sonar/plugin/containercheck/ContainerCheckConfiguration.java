package uk.gov.justice.digital.sonar.plugin.containercheck;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants;

public final class ContainerCheckConfiguration {

    private ContainerCheckConfiguration() {
        // Utility Class prevent instantiation
    }

    public static List<PropertyDefinition> getPropertyDefinitions() {
        return Arrays.asList(
            PropertyDefinition.builder(ContainerCheckConstants.SKIP_PROPERTY)
                .subCategory("General")
                .name("Skip")
                .description("When enabled we skip this plugin.")
                .defaultValue(Boolean.FALSE.toString())
                .type(PropertyType.BOOLEAN)
                .build()
        );
    }
}
