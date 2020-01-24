package uk.gov.justice.digital.sonar.plugin.containercheck.parser;

public class ReportParserException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ReportParserException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }
}
