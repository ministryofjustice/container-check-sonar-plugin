package uk.gov.justice.digital.sonar.plugin.containercheck.page;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.PAGE_KEY;
import static uk.gov.justice.digital.sonar.plugin.containercheck.base.ContainerCheckConstants.PAGE_NAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;

class ContainerCheckReportPageTest {

    @DisplayName("Building the container check report page")
    @Test
    void testBuildPage() {
        final Context context = new Context();

        // Act
        new ContainerCheckReportPage().define(context);

        // Assert
        assertEquals(1, context.getPages().size());
        final Page page = context.getPages().iterator().next();
        assertEquals(Scope.COMPONENT, page.getScope());
        assertEquals(PAGE_NAME, page.getName());
        assertEquals(PAGE_KEY, page.getKey());
        assertFalse(page.isAdmin());
    }
}
