
## Container Vulnerability Plugin for SonarQube 7.9.x
This project creates a SonarQube plugin which is capable of taking Trivy-generated vulnerablility scans on docker container images and upload that data for presentation in SonarQube. It wil typically be installed as a plugin in SonarQube and will be activated by Sonar Scanner which will be configured with the location of the Trivy report file and the HTTP URL for the SonarQube. 

### Description
Once a SonarQube installation has been created, it is necessary to run a scanner across a target project. The scanner will perform analysis and upload the results of that analysis to SonarQube. Scanners exist for standard build tools such as Jenkins, Gradle and Maven. SonarQube supports many built-in types of analysis for various languages.

SonarQube can also be extended by means of its plugin mechanism. 

The plugin depends upon the existence of a Trivy - generated vulnerability report in JSON format. For details about how to generate such a report, consult the Trivy site https://github.com/aquasecurity/trivy

### Runtime Configuration

A typical SonarQube configuration will have the following parameter to specify the location of the Trivy JSON report. This example assumes the use of a Jenkins workspace, but can easily be altered for other CI/CD systems.

```ini
sonar.containerCheck.jsonReportPath=${WORKSPACE}/container-check-report.json
```

If you want skip this plugin, it's possible with following configuration.

```ini
sonar.containerCheck.skip=true
sonar.containerCheck.skip=false (default)
```

## Releasing

The project uses maven to build and deploy.

### Local build

Create the Plugin JAR for local use, the JAR will be
```bash
mvn clean package
```

### Deploy Release to GitHub 

Deployment to GitHub
```bash
mvn release:prepare release:perform
```

### References

https://docs.sonarqube.org/7.9/analysis/overview/ - General SonarQube documents

https://docs.sonarqube.org/7.9/extend/developing-plugin/ - Information about creating and running plugins




