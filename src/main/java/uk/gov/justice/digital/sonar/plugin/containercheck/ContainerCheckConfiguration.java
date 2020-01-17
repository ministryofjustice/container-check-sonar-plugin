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
        PropertyDefinition.builder(ContainerCheckConstants.SEVERITY_CRITICAL)
            .subCategory(ContainerCheckConstants.SUB_CATEGORY_SEVERITIES)
            .name("Critical")
            .description("Minimum score for critical issues or -1 to deactivate critical issues.")
            .defaultValue(Float.toString(ContainerCheckConstants.SEVERITY_CRITICAL_DEFAULT))
            .type(PropertyType.FLOAT)
            .build(),
        PropertyDefinition.builder(ContainerCheckConstants.SEVERITY_MAJOR)
            .subCategory(ContainerCheckConstants.SUB_CATEGORY_SEVERITIES)
            .name("Major")
            .description("Minimum score for major issues or -1 to deactivate major issues.")
            .defaultValue(Float.toString(ContainerCheckConstants.SEVERITY_MAJOR_DEFAULT))
            .type(PropertyType.FLOAT)
            .build(),
        PropertyDefinition.builder(ContainerCheckConstants.SEVERITY_MINOR)
            .subCategory(ContainerCheckConstants.SUB_CATEGORY_SEVERITIES)
            .name("Minor")
            .description("Minimum score for minor issues or -1 to deactivate minor issues.")
            .defaultValue(Float.toString(ContainerCheckConstants.SEVERITY_MINOR_DEFAULT))
            .type(PropertyType.FLOAT)
            .build(),
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
