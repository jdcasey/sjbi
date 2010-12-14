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

package org.commonjava.sjbi.maven3.builder;

import static java.util.Collections.singletonList;
import static org.commonjava.sjbi.maven3.builder.M3PhaseMapping.mappingFor;

import org.apache.log4j.Logger;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.commonjava.emb.DefaultEMBExecutionRequest;
import org.commonjava.emb.boot.embed.EMBEmbedder;
import org.commonjava.emb.boot.embed.EMBEmbeddingException;
import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.model.ArtifactSetRef;
import org.commonjava.sjbi.model.BuildResult;
import org.commonjava.sjbi.spi.BuildMechanism;
import org.commonjava.sjbi.validation.ArtifactSetValidator;

import java.io.File;
import java.util.Collection;
import java.util.Map;

@Component( role = BuildMechanism.class, hint = Maven3Service.ID )
public class M3Mechanism
    implements BuildMechanism<M3Context>
{

    private static final Logger LOGGER = Logger.getLogger( M3Mechanism.class );

    @Requirement
    private EMBEmbedder emb;

    @Requirement( role = ArtifactSetValidator.class )
    private Map<String, ArtifactSetValidator> validators;

    public M3Mechanism( final EMBEmbedder emb, final Map<String, ArtifactSetValidator> validators )
    {
        this.emb = emb;
        this.validators = validators;
    }

    public M3Mechanism( final EMBEmbedder emb )
        throws EMBEmbeddingException
    {
        this.emb = emb;
    }

    M3Mechanism()
    {
        emb = null;
    }

    void setEmb( final EMBEmbedder emb )
    {
        this.emb = emb;
    }

    public M3Mechanism addValidator( final ArtifactSetValidator validator )
    {
        validators.put( validator.getId(), validator );
        return this;
    }

    public BuildResult build( final BuildPhase endPhase, final File projectDirectory, final M3Context context )
        throws SJBIException
    {
        final DefaultEMBExecutionRequest req = new DefaultEMBExecutionRequest();
        final M3PhaseMapping mapping = mappingFor( endPhase );
        if ( mapping == null )
        {
            throw new SJBIException( "Cannot find Maven 3 lifecycle phase mapping for BuildPhase: %s", endPhase );
        }

        final File pomFile = new File( projectDirectory, "pom.xml" );
        if ( !pomFile.exists() )
        {
            throw new SJBIException( "POM: %s doesn't exist.", pomFile );
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
            throw new SJBIException( "Build to phase: %s failed. Reason: %s", e, endPhase.name(), e.getMessage() );
        }

        final M3BuildResult br = new M3BuildResult( result );
        validateArtifacts( projectDirectory, br );

        return br;
    }

    private void validateArtifacts( final File projectDir, final M3BuildResult result )
    {
        final Collection<ArtifactSetRef> refs = result.getArtifactSets();

        if ( validators != null && !validators.isEmpty() )
        {
            for ( final ArtifactSetRef ref : refs )
            {
                for ( final Map.Entry<String, ArtifactSetValidator> entry : validators.entrySet() )
                {
                    final String validatorId = entry.getKey();
                    final ArtifactSetValidator validator = entry.getValue();

                    if ( !validator.isValid( ref, result ) )
                    {
                        LOGGER.error( "Validator: " + validatorId + " marked artifact-set INVALID: " + ref );
                        break;
                    }
                }
            }
        }
    }

}
