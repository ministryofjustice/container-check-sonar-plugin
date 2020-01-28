package uk.gov.justice.digital.sonar.plugin.containercheck.base;

public final class ContainerCheckConstants {

  public static final String JSON_REPORT_PATH_PROPERTY = "sonar.containerCheck.jsonReportPath";
  public static final String DOCKERFILE_PATH_PROPERTY = "sonar.containerCheck.dockerFilePath";
  public static final String SKIP_PROPERTY = "sonar.containerCheck.skip";

  public static final String JSON_REPORT_PATH_DEFAULT = "${WORKSPACE}/container-check-report.json";

  public static final String REPOSITORY_KEY = "TRIVY";
  public static final String LANGUAGE_KEY = "neutral";
  public static final String RULE_KEY = "ContainerPackageWithVulnerability";

  public static final String PAGE_NAME = "Container-Check";
  public static final String PAGE_KEY = "containercheck/report";

  private ContainerCheckConstants() {
  }

}
