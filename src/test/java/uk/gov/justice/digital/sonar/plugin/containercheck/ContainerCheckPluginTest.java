/*
 * Dependency-Check Plugin for SonarQube
 * Copyright (C) 2015-2019 dependency-check
 * philipp.dallig@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package uk.gov.justice.digital.sonar.plugin.containercheck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.PluginContextImpl;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

class ContainerCheckPluginTest
{
    @Test
    public void testExtensions() {
        final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(7, 9),
                                                                    SonarQubeSide.SCANNER, SonarEdition.COMMUNITY);
        final Plugin.Context context = new PluginContextImpl.Builder().setSonarRuntime(runtime).build();
        final ContainerCheckPlugin plugin = new ContainerCheckPlugin();

        // Act
        plugin.define(context);

        // Assert
        assertEquals(6, context.getExtensions().size());
    }
}
