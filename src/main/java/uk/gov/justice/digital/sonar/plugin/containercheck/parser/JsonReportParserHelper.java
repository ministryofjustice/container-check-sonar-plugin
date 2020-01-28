package uk.gov.justice.digital.sonar.plugin.containercheck.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import uk.gov.justice.digital.sonar.plugin.containercheck.report.JsonReportFile;

public final class JsonReportParserHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonReportParserHelper() {
        // do nothing
    }

    public static <T> List<T> parse(final JsonReportFile file, final Class<T> clazz) throws ReportParserException {
        try (InputStream inputStream = file.getInputStream()) {
            return MAPPER.readValue(inputStream,
                                MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (final IOException e) {
            throw new ReportParserException("IO Problem with JSON-Report", e);
        }
    }

}
