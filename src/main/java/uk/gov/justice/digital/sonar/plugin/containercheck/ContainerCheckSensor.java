package uk.gov.justice.digital.sonar.plugin.containercheck;

import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.DOCKERFILE_PATH_PROPERTY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.SKIP_PROPERTY;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.scanner.sensor.ProjectSensor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;
import uk.gov.justice.digital.sonar.plugin.containercheck.model.Analysis;
import uk.gov.justice.digital.sonar.plugin.containercheck.parser.JsonReportParserHelper;
import uk.gov.justice.digital.sonar.plugin.containercheck.parser.ReportParserException;
import uk.gov.justice.digital.sonar.plugin.containercheck.reason.ContainerImageDependencyReason;
import uk.gov.justice.digital.sonar.plugin.containercheck.reason.DependencyReason;
import uk.gov.justice.digital.sonar.plugin.containercheck.report.JsonReportFile;

public class ContainerCheckSensor implements ProjectSensor {

    private static final Logger LOGGER = Loggers.get(ContainerCheckSensor.class);
    private static final String SENSOR_NAME = "Container-Check";

    private final FileSystem fileSystem;
    private final PathResolver pathResolver;
    private final Function<SensorContext, DependencyReason> dependencyBuilder;

    /**
     * Constructor used at runtime by Sonar Scanner / SonarQube.
     * @param fileSystem
     *          The file system which describes where the scanner is running
     * @param pathResolver
     *          A path resolver to locate files
     * @param sensorContext
     *          The sensor context
     */
    public ContainerCheckSensor(final FileSystem fileSystem, final PathResolver pathResolver, final SensorContext sensorContext) {
        this.fileSystem = fileSystem;
        this.pathResolver = pathResolver;
        this.dependencyBuilder = (context -> {
            final String dockerPath = context.config().get(DOCKERFILE_PATH_PROPERTY).orElse("**/Dockerfile");
            final FilePredicate filePredicate = context.fileSystem().predicates().matchesPathPattern(dockerPath);
            final Iterator<InputFile> files = context.fileSystem().inputFiles(filePredicate).iterator();
            final ContainerImageDependencyReason dependencyReason;
            if (!files.hasNext()) {
                LOGGER.warn("No Dockerfiles found matching path property {}. ", dockerPath);
                dependencyReason = null;
            }
            else {
                dependencyReason = new ContainerImageDependencyReason(files.next());
                if (files.hasNext()) {
                    LOGGER.warn("Multiple Dockerfiles found matching path property {}. ", dockerPath);
                }
            }
            return dependencyReason;
        });
    }

    /**
     * Constructor used at runtime by Sonar Scanner / SonarQube.
     * @param fileSystem
     *          The file system which describes where the scanner is running
     * @param pathResolver
     *          A path resolver to locate files
     * @param dependencyBuilder
     *          Depedency builder
     */
    ContainerCheckSensor(final FileSystem fileSystem,
                        final PathResolver pathResolver,
                        final Function<SensorContext, DependencyReason> dependencyBuilder) {
        this.fileSystem = fileSystem;
        this.pathResolver = pathResolver;
        this.dependencyBuilder = dependencyBuilder;
    }

    /**
     * Takes the JSON file specified in config, parses and returns a list of {@link Analysis} instances. Guaranteed to
     * return an empty list in the event of a problem.
     *
     * @param context the {@link SensorContext} instance
     * @return a list of {@link Analysis} instances, guaranteed non-null return value
     */
    private List<Analysis> parseAnalysis(final SensorContext context) {
        try {
            final JsonReportFile jsonReportFile = JsonReportFile.getJsonReportFile(context.config(), fileSystem, pathResolver);
            final List<Analysis> analyses = JsonReportParserHelper.parse(jsonReportFile, Analysis.class);
            return analyses == null ? Collections.emptyList() : analyses;
        } catch (final FileNotFoundException e) {
            LOGGER.error("JSON-Analysis skipped/aborted due to missing report file");
            LOGGER.debug(e.getMessage(), e);
        } catch (final ReportParserException e) {
            LOGGER.error("JSON-Analysis aborted");
            LOGGER.debug(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return SENSOR_NAME;
    }

    @Override
    public void describe(final SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name(SENSOR_NAME);
    }

    @Override
    public void execute(final SensorContext sensorContext) {

        if (skipConditionsApply(sensorContext.config())) {
            return;
        }

        final Profiler profiler = Profiler.create(LOGGER);
        profiler.startInfo("Process Container-Check report");
        final List<Analysis> analyses = parseAnalysis(sensorContext);
        final DependencyReason dependencyReason = dependencyBuilder.apply(sensorContext);
        for (final Analysis analysis : analyses) {
            dependencyReason.addIssues(sensorContext, analysis);
        }
        profiler.stopInfo();
    }

    boolean skipConditionsApply(final  org.sonar.api.config.Configuration configuration) {
        boolean skip = false;
        if (configuration.getBoolean(SKIP_PROPERTY).orElse(Boolean.FALSE).booleanValue()) {
            LOGGER.info("Container-Check skipped, skip property {} set to true", SKIP_PROPERTY);
            skip = true;
        }
        if (dependencyBuilder == null) {
            LOGGER.info("Container-Check skipped, no Dockerfiles found.");
            skip = true;
        }
        return skip;
    }

}
