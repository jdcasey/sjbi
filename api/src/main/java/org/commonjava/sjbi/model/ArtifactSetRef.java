/*
 * Copyright (c) 2010 Red Hat, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see 
 * <http://www.gnu.org/licenses>.
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

    @Override
    public String toString()
    {
        return projectRef.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( projectRef == null ) ? 0 : projectRef.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final ArtifactSetRef other = (ArtifactSetRef) obj;
        if ( projectRef == null )
        {
            if ( other.projectRef != null )
            {
                return false;
            }
        }
        else if ( !projectRef.equals( other.projectRef ) )
        {
            return false;
        }
        return true;
    }

}
