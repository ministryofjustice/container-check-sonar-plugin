
## Container Vulnerability Plugin for SonarQube 7.9.x
This project creates a SonarQube plugin which is capable of taking Trivy-generated vulnerablility scans on docker container images and upload that data for presentation in SonarQube. It wil typically be installed as a plugin in SonarQube and will be activated by Sonar Scanner which will be configured with the location of the Trivy report file and the HTTP URL for the SonarQube. 

### Description
Once a SonarQube installation has been created, it is necessary to run a scanner across a target project. The scanner will perform analysis and upload the results of that analysis to SonarQube. Scanners exist for standard build tools such as Jenkins, Gradle and Maven. SonarQube supports many built-in types of analysis for various languages.

SonarQube can also be extended by means of its plugin mechanism. 

The plugin depends upon the existence of a Trivy - generated vulnerability report in JSON format. For details about how to generate such a report, consult the Trivy site https://github.com/aquasecurity/trivy

### Runtime Configuration

A client project will typically hold a properties file, for example  named sonar-project.properties which tells the sonar scanner how it should run. The following properties are generally required.

```ini
# must be unique in a given SonarQube instance
sonar.projectKey=[unique string to identify the project]
# this is the name displayed in the SonarQube UI
sonar.projectName=[A more descriptive name which is presented on the SonarQube UI]
```

There are additional properties available for this plugin. This following parameter to specify the location of the Trivy JSON report. 

```ini
sonar.containerCheck.jsonReportPath=./container-check-report.json
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

The target artifact for the project is a JAR file which can be deployed as a plugin to SonarQube. The result of the maven package command will  be the Plugin JAR.

#### Manual

It is possible to manually release the JAR file into GitHub. This is done by logging into GitHub, going to "https://github.com/ministryofjustice/container-check-sonar-plugin/releases" and following the screens which follow from "Draft a new release".

#### Maven (git-release-plugin)

Alternatively, and following the example used in the dependency-check plugin (https://github.com/dependency-check/dependency-check-sonar-plugin), it is possible to upload and tag the release directly though a configured maven plugin with this maven command. It is necessary to create a personal access token (PAT) with "repo" and "admin:org" permissions. PATs are necessary because of the multi-factor authentication required by HMPPS and are required by clients which use the GitHub API. See https://github.com/settings/tokens to start. SSO must be enabled on these tokens. The version nmber applied in the release will the version defined in the pom.

```bash
mvn de.jutzig:github-release-plugin:1.4.0:release -Dusername=[username] -Dpassword=[Personal Access Token]
```

### References

https://docs.sonarqube.org/7.9/analysis/overview/ - General SonarQube documents

https://docs.sonarqube.org/7.9/extend/developing-plugin/ - Information about creating and running plugins




