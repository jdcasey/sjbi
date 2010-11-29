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

package org.commonjava.sjbi.maven3.builder;

import static java.util.Collections.singletonList;
import static org.commonjava.sjbi.maven3.builder.M3PhaseMapping.mappingFor;

import org.apache.maven.execution.MavenExecutionResult;
import org.commonjava.emb.DefaultEMBExecutionRequest;
import org.commonjava.emb.boot.embed.EMBEmbedder;
import org.commonjava.emb.boot.embed.EMBEmbedderBuilder;
import org.commonjava.emb.boot.embed.EMBEmbeddingException;
import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.builder.BuildCapabilities;
import org.commonjava.sjbi.builder.BuildException;
import org.commonjava.sjbi.builder.BuildMechanism;
import org.commonjava.sjbi.builder.BuildResult;

import java.io.File;

public class M3Builder
    implements BuildMechanism<M3Context>
{

    private final EMBEmbedder emb;

    public M3Builder( final EMBEmbedder emb )
    {
        this.emb = emb;
    }

    public M3Builder()
        throws EMBEmbeddingException
    {
        emb = new EMBEmbedderBuilder().build();
    }

    public BuildResult build( final BuildPhase endPhase, final File projectDirectory, final M3Context context )
        throws BuildException
    {
        final DefaultEMBExecutionRequest req = new DefaultEMBExecutionRequest();
        final M3PhaseMapping mapping = mappingFor( endPhase );
        if ( mapping == null )
        {
            throw new BuildException( "Cannot find Maven 3 lifecycle phase mapping for BuildPhase: %s", endPhase );
        }

        final File pomFile = new File( projectDirectory, "pom.xml" );
        if ( !pomFile.exists() )
        {
            throw new BuildException( "POM: %s doesn't exist.", pomFile );
        }

        req.setGoals( singletonList( mapping.m3Phase() ) );
        req.setPom( pomFile );

        final MavenExecutionResult result;
        try
        {
            result = emb.execute( req );
        }
        catch ( final EMBEmbeddingException e )
        {
            throw new BuildException( "Build to phase: %s failed. Reason: %s", e, endPhase.name(), e.getMessage() );
        }

        return new M3BuildResult( result );
    }

    public BuildCapabilities getBuildCapabilities()
    {
        return M3Capabilities.INSTANCE;
    }

}
