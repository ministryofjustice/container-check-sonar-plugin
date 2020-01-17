package uk.gov.justice.digital.sonar.plugin.containercheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.DOCKERFILE_PATH_PROPERTY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.JSON_REPORT_PATH_PROPERTY;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import uk.gov.justice.digital.sonar.plugin.containercheck.reason.ContainerImageDependencyReason;
import uk.gov.justice.digital.sonar.plugin.containercheck.reason.DependencyReason;

@ExtendWith(MockitoExtension.class)
class ContainerCheckSensorTest {

    private ContainerCheckSensor containercheckSensor;

    @Mock
    private PathResolver pathResolver;

    private static File sampleJsonReport;
    private SensorContextTester sensorContext;

    @BeforeAll
    static void beforeAll() throws URISyntaxException, NullPointerException {
        final URL sampleXmlResourceURI = ContainerCheckSensorTest.class.getClassLoader().getResource("trivy-sample.json");
        sampleJsonReport = Paths.get(sampleXmlResourceURI.toURI()).toFile();
    }

    @BeforeEach
    void beforeEach() {

        final MapSettings settings = new MapSettings();
        settings.setProperty(JSON_REPORT_PATH_PROPERTY, sampleJsonReport.getAbsolutePath());
        settings.setProperty(DOCKERFILE_PATH_PROPERTY, "/Users/chris-moj/moj-github/container-check-sonar-plugin/target/test-classes/Dockerfile");
        final Configuration config = settings.asConfig();

        sensorContext = SensorContextTester.create(new File(System.getProperty("user.dir")));
        sensorContext.setSettings(settings);

        final Function<SensorContext, DependencyReason> dependencyBuilder
            = (InputFile) -> { return new ContainerImageDependencyReason(null); };
        containercheckSensor = new ContainerCheckSensor(sensorContext.fileSystem(), pathResolver,  dependencyBuilder);
    }

    @Disabled
    @DisplayName("Test simple execution where there are 5 ")
    @Test
    void shouldAddAnIssueForVulnerability() {

        when(pathResolver.relativeFile(Mockito.any(File.class), anyString())).thenReturn(sampleJsonReport);

        // Act
        containercheckSensor.execute(sensorContext);

        // Assert
        assertEquals(5, sensorContext.allIssues().size());
    }
}
