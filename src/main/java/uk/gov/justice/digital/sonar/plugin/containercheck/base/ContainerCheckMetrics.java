package uk.gov.justice.digital.sonar.plugin.containercheck.base;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.scanner.ScannerSide;

@ScannerSide
public final class ContainerCheckMetrics implements Metrics {

  private static final String DOMAIN = "Container-Dependency-Check";

  private static final String TOTAL_DEPENDENCIES_KEY = DOMAIN + "_total_dependencies";
  private static final String VULNERABLE_DEPENDENCIES_KEY = DOMAIN + "_vulnerable_dependencies";

  static final String TOTAL_VULNERABILITIES_KEY = DOMAIN + "_total_vulnerabilities";
  static final String CRITICAL_SEVERITY_VULNS_KEY = DOMAIN + "_critical_severity_vulns";
  static final String HIGH_SEVERITY_VULNS_KEY = DOMAIN + "_high_severity_vulns";
  static final String MEDIUM_SEVERITY_VULNS_KEY = DOMAIN + "_medium_severity_vulns";
  static final String LOW_SEVERITY_VULNS_KEY = DOMAIN + "_low_severity_vulns";

  private static final String REPORT_KEY = DOMAIN + "_report";

  public static final Metric<Integer> CRITICAL_SEVERITY_VULNS = new Metric.Builder(
      CRITICAL_SEVERITY_VULNS_KEY, "Critical Severity Vulnerabilities", Metric.ValueType.INT)
      .setDescription("Critical Severity Vulnerabilities")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setBestValue(0.0)
      .setHidden(false)
      .create();

  public static final Metric<Integer> HIGH_SEVERITY_VULNS = new Metric.Builder(
      HIGH_SEVERITY_VULNS_KEY, "High Severity Vulnerabilities", Metric.ValueType.INT)
      .setDescription("High Severity Vulnerabilities")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setBestValue(0.0)
      .setHidden(false)
      .create();

  public static final Metric<Integer> MEDIUM_SEVERITY_VULNS = new Metric.Builder(
      MEDIUM_SEVERITY_VULNS_KEY, "Medium Severity Vulnerabilities", Metric.ValueType.INT)
      .setDescription("Medium Severity Vulnerabilities")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setBestValue(0.0)
      .setHidden(false)
      .create();

  public static final Metric<Integer> LOW_SEVERITY_VULNS = new Metric.Builder(
      LOW_SEVERITY_VULNS_KEY, "Low Severity Vulnerabilities", Metric.ValueType.INT)
      .setDescription("Low Severity Vulnerabilities")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setBestValue(0.0)
      .setHidden(false)
      .create();

  public static final Metric<Integer> TOTAL_DEPENDENCIES = new Metric.Builder(
      TOTAL_DEPENDENCIES_KEY, "Total Dependencies", Metric.ValueType.INT)
      .setDescription("Total Dependencies")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setHidden(false)
      .create();

  public static final Metric<Integer> VULNERABLE_DEPENDENCIES = new Metric.Builder(
      VULNERABLE_DEPENDENCIES_KEY, "Vulnerable Dependencies", Metric.ValueType.INT)
      .setDescription("Vulnerable Dependencies")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setBestValue(0.0)
      .setHidden(false)
      .create();

  public static final Metric<Integer> TOTAL_VULNERABILITIES = new Metric.Builder(
      TOTAL_VULNERABILITIES_KEY, "Total Vulnerabilities", Metric.ValueType.INT)
      .setDescription("Total Vulnerabilities")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setBestValue(0.0)
      .setHidden(false)
      .create();

  public static final Metric<String> REPORT = new Metric.Builder(REPORT_KEY,
      "Container-Check Report", Metric.ValueType.DATA)
      .setDescription("Report HTML")
      .setQualitative(Boolean.FALSE)
      .setDomain(ContainerCheckMetrics.DOMAIN)
      .setHidden(false)
      .setDeleteHistoricalData(true)
      .create();

  @Override
  public List<Metric> getMetrics() {
    return Arrays.asList(
        ContainerCheckMetrics.CRITICAL_SEVERITY_VULNS,
        ContainerCheckMetrics.HIGH_SEVERITY_VULNS,
        ContainerCheckMetrics.MEDIUM_SEVERITY_VULNS,
        ContainerCheckMetrics.LOW_SEVERITY_VULNS,
        ContainerCheckMetrics.TOTAL_DEPENDENCIES,
        ContainerCheckMetrics.VULNERABLE_DEPENDENCIES,
        ContainerCheckMetrics.TOTAL_VULNERABILITIES,
        ContainerCheckMetrics.REPORT
    );
  }
}
