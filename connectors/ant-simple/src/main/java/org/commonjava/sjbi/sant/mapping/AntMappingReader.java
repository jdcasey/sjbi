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

import static org.apache.commons.io.IOUtils.closeQuietly;

import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.sant.SimpleAntContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AntMappingReader
{
    public static final String FILENAME = "sjbi.json";

    public AntMapping readMapping( final File projectDirectory, final SimpleAntContext context )
        throws SJBIException
    {
        final File f = new File( projectDirectory, FILENAME );
        if ( !f.exists() || !f.isFile() )
        {
            return null;
        }

        FileReader in = null;
        try
        {
            in = new FileReader( f );

            final AntMapping mapping = newGson().fromJson( in, AntMapping.class );
            removeCommentedEntries( mapping );

            return mapping;
        }
        catch ( final IOException e )
        {
            throw new SJBIException( "Cannot read build-interface mappings from: %s. Reason: %s", e, FILENAME,
                                     e.getMessage() );
        }
        finally
        {
            closeQuietly( in );
        }
    }

    private void removeCommentedEntries( final AntMapping mapping )
    {
        if ( isComment( mapping.getBuildFile() ) )
        {
            mapping.setBuildFile( null );
        }

        final Map<BuildPhase, String> phaseMap = mapping.getPhaseMappings();
        for ( final Map.Entry<BuildPhase, String> entry : new HashMap<BuildPhase, String>( phaseMap ).entrySet() )
        {
            if ( isComment( entry.getValue() ) )
            {
                phaseMap.remove( entry.getKey() );
            }
        }

        mapping.setPhaseMappings( phaseMap );

        final Collection<AntProjectMetadata> projects = mapping.getProjects();
        for ( final AntProjectMetadata project : new ArrayList<AntProjectMetadata>( projects ) )
        {
            boolean remove = false;
            if ( isComment( project.getPom() ) || isComment( project.getCoord() ) )
            {
                remove = true;
            }
            else
            {
                final Map<String, String> artifacts = project.getArtifacts();
                for ( final Map.Entry<String, String> entry : new HashMap<String, String>( artifacts ).entrySet() )
                {
                    if ( isComment( entry.getKey() ) || isComment( entry.getValue() ) )
                    {
                        artifacts.remove( entry.getKey() );
                    }
                }

                if ( artifacts.isEmpty() )
                {
                    remove = true;
                }
            }

            if ( remove )
            {
                projects.remove( project );
            }
        }

        mapping.setProjects( projects );
    }

    private boolean isComment( final String value )
    {
        return value != null && value.startsWith( "//" );
    }

    static Gson newGson()
    {
        return new GsonBuilder().create();
    }
}
