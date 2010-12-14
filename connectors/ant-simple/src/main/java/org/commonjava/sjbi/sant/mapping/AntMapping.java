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

package org.commonjava.sjbi.sant.mapping;

import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.model.ArtifactSetRef;
import org.commonjava.sjbi.model.ProjectRef;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AntMapping
{

    @SerializedName( "projectfile" )
    private String buildFile;

    @SerializedName( "mappings" )
    private Map<BuildPhase, String> phaseMappings;

    private Collection<AntProjectMetadata> projects;

    public String getBuildFile()
    {
        return buildFile;
    }

    public void setBuildFile( final String buildFile )
    {
        this.buildFile = buildFile;
    }

    public Collection<AntProjectMetadata> getProjects()
    {
        return projects;
    }

    public void setProjects( final Collection<AntProjectMetadata> projects )
    {
        this.projects = projects;
    }

    public Map<BuildPhase, String> getPhaseMappings()
    {
        return phaseMappings;
    }

    public void setPhaseMappings( final Map<BuildPhase, String> phaseMappings )
    {
        this.phaseMappings = phaseMappings;
    }

    public Collection<ArtifactSetRef> getArtifactSetRefs( final File projectDir )
    {
        final List<ArtifactSetRef> refs = new ArrayList<ArtifactSetRef>();

        int index = 0;
        for ( final AntProjectMetadata metadata : projects )
        {
            final String[] coordParts = metadata.getCoord().split( ":" );
            if ( coordParts.length != 3 )
            {
                throw new IllegalArgumentException( "[" + index + "] Invalid GAV: '" + metadata.getCoord() + "'." );
            }

            final File pom = new File( projectDir, metadata.getPom() );

            final ProjectRef pr = new ProjectRef( coordParts[0], coordParts[1], coordParts[2] );
            pr.setPomFile( pom );

            final ArtifactSetRef asr = new ArtifactSetRef( pr );

            for ( final Map.Entry<String, String> entry : metadata.getArtifacts().entrySet() )
            {
                final String sk = entry.getKey();
                final File artifactFile = new File( projectDir, entry.getValue() );

                if ( sk.indexOf( ':' ) > 0 )
                {
                    final String[] parts = sk.split( ":" );
                    if ( parts.length != 2 )
                    {
                        throw new IllegalArgumentException( "[" + index + "] Invalid 'type:classifier' sub-key: '" + sk
                                        + "'." );
                    }

                    asr.addArtifactRef( parts[0], parts[1], artifactFile );
                }
                else
                {
                    asr.addArtifactRef( sk, artifactFile );
                }
            }

            refs.add( asr );
            index++;
        }

        return refs;
    }

}
