package uk.gov.justice.digital.sonar.plugin.containercheck.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Analysis {

    /**
     * Name of the container or a package in a container that Trivy analysed.
     */
    private final String target;

    /**
     * Vulnerabilities found by Trivy.
     */
    private final List<Vulnerability> vulnerabilities;

    @JsonCreator
    public Analysis(
        @JsonProperty(value = "Target", required = true) final String target,
        @JsonProperty(value = "Vulnerabilities") final List<Vulnerability> vulnerabilities) {
        this.target = target;
        this.vulnerabilities = vulnerabilities == null ? Collections.emptyList() : vulnerabilities;
    }


}
