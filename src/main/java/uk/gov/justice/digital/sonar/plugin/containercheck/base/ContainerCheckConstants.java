package uk.gov.justice.digital.sonar.plugin.containercheck.base;

public final class ContainerCheckConstants {

  public static final String JSON_REPORT_PATH_PROPERTY = "sonar.containerCheck.jsonReportPath";
  public static final String DOCKERFILE_PATH_PROPERTY = "sonar.containerCheck.dockerFilePath";

  public static final String SEVERITY_CRITICAL = "sonar.containerCheck.severity.critical";
  public static final String SEVERITY_MAJOR = "sonar.containerCheck.severity.major";
  public static final String SEVERITY_MINOR = "sonar.containerCheck.severity.minor";

  public static final String SKIP_PROPERTY = "sonar.containerCheck.skip";

  public static final Float SEVERITY_CRITICAL_DEFAULT = 7.0f;
  public static final Float SEVERITY_MAJOR_DEFAULT = 4.0f;
  public static final Float SEVERITY_MINOR_DEFAULT = 0.0f;

  public static final String JSON_REPORT_PATH_DEFAULT = "${WORKSPACE}/container-check-report.json";

  public static final String REPOSITORY_KEY = "TRIVY";
  public static final String LANGUAGE_KEY = "neutral";
  public static final String RULE_KEY = "ContainerPackageWithVulnerability";
  public static final String SUB_CATEGORY_SEVERITIES = "Severities";

  public static final String PAGE_NAME = "Container-Check";
  public static final String PAGE_KEY = "containercheck/report";

  private ContainerCheckConstants() {
  }

}
