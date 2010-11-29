/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.commonjava.sjbi.model;

import java.io.File;

public class ProjectRef
{

    private final String groupId;

    private final String artifactId;

    private final String version;

    private File pomFile;

    public ProjectRef( final String groupId, final String artifactId, final String version )
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String coordKey()
    {
        return groupId + ":" + artifactId + ":" + version;
    }

    public String artifactKey( final String type, final String classifier )
    {
        return groupId + ":" + artifactId + ":" + type + ":" + ( classifier == null ? "" : classifier + ":" ) + version;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public String getVersion()
    {
        return version;
    }

    public File getPomFile()
    {
        return pomFile;
    }

    public void setPomFile( final File pomFile )
    {
        this.pomFile = pomFile;
    }

}
