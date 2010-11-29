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
import java.util.HashMap;
import java.util.Map;

public class ArtifactSetRef
{

    private final ProjectRef projectRef;

    private final Map<String, ArtifactRef> artifactRefs = new HashMap<String, ArtifactRef>();

    public ArtifactSetRef( final ProjectRef projectRef )
    {
        this.projectRef = projectRef;
    }

    public ProjectRef getProjectRef()
    {
        return projectRef;
    }

    public ArtifactSetRef addArtifactRef( final ArtifactRef ref )
    {
        if ( projectRef.equals( ref.getProjectRef() ) )
        {
            artifactRefs.put( ref.subKey(), ref );
        }

        return this;
    }

    public ArtifactSetRef addArtifactRef( final String type, final File artifactFile )
    {
        final ArtifactRef ref = new ArtifactRef( projectRef, type );
        ref.setArtifactFile( artifactFile );

        artifactRefs.put( ref.subKey(), ref );

        return this;
    }

    public ArtifactSetRef addArtifactRef( final String type, final String classifier, final File artifactFile )
    {
        final ArtifactRef ref = new ArtifactRef( projectRef, type, classifier );
        ref.setArtifactFile( artifactFile );

        artifactRefs.put( ref.subKey(), ref );

        return this;
    }

    public Map<String, ArtifactRef> getArtifactsBySubKey()
    {
        return new HashMap<String, ArtifactRef>( artifactRefs );
    }

}
