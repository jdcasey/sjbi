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

import org.apache.commons.io.IOUtils;
import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.spi.SJBIMappingGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AntMappingWriter
    implements SJBIMappingGenerator
{

    // Standard Ant approach
    private static final String STD_BUILD_XML = "build.xml";

    // Maven 1
    private static final String PROJECT_XML = "project.xml";

    // Maven 1
    private static final String MAVEN_XML = " maven.xml";

    // Maven 2 & 3
    private static final String POM_XML = "pom.xml";

    private static Set<String> BLACKLISTED_GUESSES = new HashSet<String>( Arrays.asList( new String[] { PROJECT_XML,
        MAVEN_XML, POM_XML } ) );

    public File generateMappingsFile( final File projectDirectory )
        throws SJBIException
    {
        String projectFilename = STD_BUILD_XML;

        final File projectFile = new File( projectDirectory, projectFilename );
        if ( !projectFile.exists() || !projectFile.isFile() )
        {
            for ( final String fname : projectDirectory.list() )
            {
                if ( fname.endsWith( ".xml" ) && !BLACKLISTED_GUESSES.contains( fname ) )
                {
                    projectFilename = fname;
                    break;
                }
            }
        }

        final File mappingFile = new File( projectDirectory, AntMappingReader.FILENAME );

        final AntMapping mapping = new AntMapping();
        mapping.setBuildFile( projectFilename );

        // TODO: Read the build file, if found, to see if we can find a more intelligent way to guess at these...
        final Map<BuildPhase, String> map = new LinkedHashMap<BuildPhase, String>();
        map.put( BuildPhase.CLEAN, "clean" );
        map.put( BuildPhase.PACKAGE, "build" );
        map.put( BuildPhase.VERIFY, "dist" );
        map.put( BuildPhase.INSTALL, "// UNKNOWN" );
        map.put( BuildPhase.DEPLOY, "// UNKNOWN" );

        mapping.setPhaseMappings( map );

        final AntProjectMetadata apm = new AntProjectMetadata();
        apm.setPom( "// pom.xml" );
        apm.setCoord( "// org.myproject:some-artifact:1.0" );
        apm.setArtifacts( Collections.singletonMap( "jar", "// dist/some-artifact.jar" ) );

        mapping.setProjects( Collections.singleton( apm ) );

        writeMapping( mapping, mappingFile );

        return mappingFile;
    }

    public void writeMapping( final AntMapping mapping, final File target )
        throws SJBIException
    {
        File mappingFile = target;
        if ( mappingFile.isDirectory() )
        {
            mappingFile = new File( mappingFile, AntMappingReader.FILENAME );
        }

        FileWriter writer = null;
        try
        {
            writer = new FileWriter( mappingFile );
            writer.write( AntMappingReader.newGson().toJson( mapping, AntMapping.class ) );
        }
        catch ( final IOException e )
        {
            throw new SJBIException( "Failed to write mappings file: %s\nReason: %s", e, target, e.getMessage() );
        }
        finally
        {
            IOUtils.closeQuietly( writer );
        }
    }

}
