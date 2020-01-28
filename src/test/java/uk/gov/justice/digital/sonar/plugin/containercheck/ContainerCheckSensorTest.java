package uk.gov.justice.digital.sonar.plugin.containercheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.digital.sonar.plugin.containercheck.ContainerCheckSensor.SENSOR_NAME;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.JSON_REPORT_PATH_PROPERTY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.SKIP_PROPERTY;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import uk.gov.justice.digital.sonar.plugin.containercheck.reason.ContainerImageDependencyReason;
import uk.gov.justice.digital.sonar.plugin.containercheck.reason.DependencyReason;

class ContainerCheckSensorTest {

    private ContainerCheckSensor containerCheckSensor;

    @Mock
    private PathResolver pathResolver;

    @Mock
    private InputFile inputFile;

    private static File sampleJsonReport;
    private SensorContextTester sensorContext;

    @BeforeAll
    static void beforeAll() throws URISyntaxException, NullPointerException {
        final URL sampleXmlResourceURI = ContainerCheckSensorTest.class.getClassLoader().getResource("trivy-sample.json");
        sampleJsonReport = Paths.get(sampleXmlResourceURI.toURI()).toFile();
    }

    @BeforeEach
    void beforeEach() {

        MockitoAnnotations.initMocks(this);

        final MapSettings settings = new MapSettings();
        settings.setProperty(JSON_REPORT_PATH_PROPERTY, sampleJsonReport.getAbsolutePath());

        sensorContext = SensorContextTester.create(new File(System.getProperty("user.dir")));
        sensorContext.setSettings(settings);

        when(inputFile.isFile()).thenReturn(Boolean.TRUE);
        when(inputFile.key()).thenReturn("TRIVY");
        final Function<SensorContext, DependencyReason> dependencyBuilder
            = (InputFile) -> new ContainerImageDependencyReason(inputFile);
        containerCheckSensor = new ContainerCheckSensor(sensorContext.fileSystem(), pathResolver,  dependencyBuilder);
    }

    @DisplayName("Test simple execution where there are 5 as defined in the sample file")
    @Test
    void shouldAddAnIssueForVulnerability() {

        when(pathResolver.relativeFile(Mockito.any(File.class), anyString())).thenReturn(sampleJsonReport);

        // Act
        containerCheckSensor.execute(sensorContext);

        // Assert
        assertEquals(5, sensorContext.measures("TRIVY").size());
    }

    @DisplayName("Skip the sensor execution because of the property")
    @Test
    void skipBecauseOfProperty() {

        final MapSettings settings = new MapSettings();
        settings.setProperty(SKIP_PROPERTY, Boolean.TRUE.toString());
        sensorContext.setSettings(settings);

        // Act
        containerCheckSensor.execute(sensorContext);

        // Assert
        assertEquals(0, sensorContext.measures("TRIVY").size());
        verifyNoInteractions(pathResolver);
    }

    @DisplayName("Skip the sensor execution because there's no Dockerfile to hang vulnerabilities on")
    @Test
    void skipBecauseOfNoDockerfile() {

        containerCheckSensor = new ContainerCheckSensor(sensorContext.fileSystem(), pathResolver, sensorContext);
        // Act
        containerCheckSensor.execute(sensorContext);

        // Assert
        assertEquals(0, sensorContext.measures("TRIVY").size());
        verifyNoInteractions(pathResolver);
    }

    @DisplayName("Test toString")
    @Test
    void testToString() {
        assertEquals(SENSOR_NAME, containerCheckSensor.toString());
    }

    @DisplayName("Test describe")
    @Test
    void testDescribe() {
        final DefaultSensorDescriptor sensorDescriptor = new DefaultSensorDescriptor();

        containerCheckSensor.describe(sensorDescriptor);

        assertEquals(SENSOR_NAME, sensorDescriptor.name());
    }
}
