package uk.gov.justice.digital.sonar.plugin.containercheck.model;

/**
 * Models the possible severities supplied by the Trivy plugin. Supplies the ability to convert these to
 * equivalent severity in SonarQube.
 */
public enum Severity {

  UNKNOWN("UNKNOWN") {
    public org.sonar.api.batch.rule.Severity asSonar() {
      return org.sonar.api.batch.rule.Severity.MAJOR;
    }
  },
  LOW("LOW") {
    public org.sonar.api.batch.rule.Severity asSonar() {
      return org.sonar.api.batch.rule.Severity.INFO;
    }
  },
  MEDIUM("MEDIUM") {
    public org.sonar.api.batch.rule.Severity asSonar() {
      return org.sonar.api.batch.rule.Severity.MINOR;
    }
  },
  HIGH("HIGH") {
    public org.sonar.api.batch.rule.Severity asSonar() {
      return org.sonar.api.batch.rule.Severity.MAJOR;
    }
  },
  CRITICAL("CRITICAL") {
    public org.sonar.api.batch.rule.Severity asSonar() {
      return org.sonar.api.batch.rule.Severity.CRITICAL;
    }
  };

  Severity(final String sev) {
    this.severity = sev;
  }

  private final String severity;

  public abstract org.sonar.api.batch.rule.Severity asSonar();

  public String getSeverity() {
    return severity;
  }
}
