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

public class ArtifactRef
{

    private final ProjectRef projectRef;

    private File artifactFile;

    private final String type;

    private final String classifier;

    public ArtifactRef( final ProjectRef projectRef, final String type, final String classifier )
    {
        this.projectRef = projectRef;
        this.type = type;
        this.classifier = classifier;
    }

    public ArtifactRef( final ProjectRef projectRef, final String type )
    {
        this.projectRef = projectRef;
        this.type = type;
        classifier = null;
    }

    public String subKey()
    {
        return type + ( classifier != null ? ":" + classifier : "" );
    }

    public String key()
    {
        return projectRef.artifactKey( type, classifier );
    }

    public File getArtifactFile()
    {
        return artifactFile;
    }

    public void setArtifactFile( final File artifactFile )
    {
        this.artifactFile = artifactFile;
    }

    public ProjectRef getProjectRef()
    {
        return projectRef;
    }

    public String getType()
    {
        return type;
    }

    public String getClassifier()
    {
        return classifier;
    }

}
