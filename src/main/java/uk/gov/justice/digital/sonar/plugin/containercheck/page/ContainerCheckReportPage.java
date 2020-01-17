package uk.gov.justice.digital.sonar.plugin.containercheck.page;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;
import uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants;

public class ContainerCheckReportPage implements PageDefinition {

    @Override
    public void define(final Context context) {
        context.addPage(
            Page.builder(ContainerCheckConstants.PAGE_KEY)
                .setScope(Scope.COMPONENT)
                .setComponentQualifiers(Page.Qualifier.PROJECT, Page.Qualifier.MODULE)
                .setName(ContainerCheckConstants.PAGE_NAME)
                .setAdmin(false)
                .build());
    }
}
