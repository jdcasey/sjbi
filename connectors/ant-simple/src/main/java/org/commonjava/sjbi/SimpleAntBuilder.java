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

package org.commonjava.sjbi;

import static org.apache.commons.lang.StringUtils.join;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DemuxInputStream;
import org.apache.tools.ant.DemuxOutputStream;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.commonjava.sjbi.builder.BuildCapabilities;
import org.commonjava.sjbi.builder.BuildException;
import org.commonjava.sjbi.builder.BuildMechanism;
import org.commonjava.sjbi.builder.BuildResult;
import org.commonjava.sjbi.mapping.AntMapping;
import org.commonjava.sjbi.mapping.JSONReader;
import org.commonjava.sjbi.model.ArtifactSetRef;
import org.commonjava.sjbi.validation.ArtifactSetValidator;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

@Component( role = BuildMechanism.class, hint = "ant-simple" )
public class SimpleAntBuilder
    implements BuildMechanism<SimpleAntContext>
{
    private static final Logger LOGGER = Logger.getLogger( SimpleAntBuilder.class );

    @Requirement( role = ArtifactSetValidator.class )
    private Map<String, ArtifactSetValidator> validators;

    public BuildCapabilities getBuildCapabilities()
    {
        return SimpleAntCapabilities.INSTANCE;
    }

    /**
     * @see {@link org.apache.tools.ant.Main#runBuild(java.lang.ClassLoader)}
     * 
     *      {@inheritDoc}
     * @see org.commonjava.sjbi.builder.BuildMechanism#build(org.commonjava.sjbi.BuildPhase, java.io.File,
     *      org.commonjava.sjbi.builder.BuildContext)
     */
    // TODO: Thread safety??
    public BuildResult build( final BuildPhase endPhase, final File projectDirectory, final SimpleAntContext context )
        throws BuildException
    {
        final AntMapping antMapping = new JSONReader().readMapping( projectDirectory, context );
        final SimpleAntResult result = new SimpleAntResult();

        String[] targets;
        try
        {
            targets = getTargets( antMapping, endPhase );
        }
        catch ( final BuildException e )
        {
            result.addError( e );
            return result;
        }

        final File buildFile = new File( projectDirectory, antMapping.getBuildFile() );

        build( buildFile, targets, result );
        validateAndSetArtifacts( antMapping, projectDirectory, result );

        return result;
    }

    private void validateAndSetArtifacts( final AntMapping antMapping, final File projectDir,
                                          final SimpleAntResult result )
    {
        final Collection<ArtifactSetRef> refs = antMapping.getArtifactSetRefs( projectDir );
        result.setArtifactSets( refs );

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

    private String[] getTargets( final AntMapping antMapping, final BuildPhase endPhase )
        throws BuildException
    {
        final Map<BuildPhase, String> mappings = antMapping.getPhaseMappings();

        String targetStr = null;
        BuildPhase ep = endPhase;

        do
        {
            targetStr = mappings.get( endPhase );
            if ( targetStr == null )
            {
                ep = endPhase.previousNotClean();
            }
            else
            {
                LOGGER.warn( "No Ant targets bound to build-phase: " + endPhase + "; using mappings for: " + ep
                                + " instead.\n\nUsing Ant target(s): " + targetStr );
            }
        }
        while ( targetStr == null && ep != null );

        String[] targets;
        if ( targetStr != null )
        {
            targets = targetStr.split( "\\s*,?\\s*" );

            int idx = 0;
            for ( final String target : targets )
            {
                targets[idx] = target.trim();
                idx++;
            }
        }
        else
        {
            throw new BuildException(
                                      "No Ant targets specified for build phase: %s (or lower). Please add a 'mappings' section to %s "
                                                      + "in order to associate one or more Ant targets with each of: %s.",
                                      endPhase, JSONReader.FILENAME, BuildPhase.listing() );
        }

        return targets;
    }

    private void build( final File buildFile, final String[] targets, final SimpleAntResult result )
    {
        final InputStream origIn = System.in;
        final PrintStream origOut = System.out;
        final PrintStream origErr = System.err;

        final Project project = new Project();

        Throwable error = null;
        try
        {
            project.addBuildListener( new DefaultLogger() );
            project.setDefaultInputStream( System.in );

            System.setIn( new DemuxInputStream( project ) );
            System.setOut( new PrintStream( new DemuxOutputStream( project, false ) ) );
            System.setErr( new PrintStream( new DemuxOutputStream( project, true ) ) );

            project.fireBuildStarted();
            project.init();

            // TODO: Handle properties, via context...?
            // project.setUserProperty(arg, value);

            project.setUserProperty( MagicNames.ANT_FILE, buildFile.getAbsolutePath() );
            project.setUserProperty( MagicNames.ANT_FILE_TYPE, MagicNames.ANT_FILE_TYPE_FILE );

            project.setKeepGoingMode( false );

            ProjectHelper.configureProject( project, buildFile );
            project.executeTargets( new Vector<String>( Arrays.asList( targets ) ) );

        }
        catch ( final RuntimeException e )
        {
            error = e;
            result.addError( new BuildException( "Error occurred in Ant build: %s\nTargets: [%s]\nBuild file: %s", e,
                                                 e.getMessage(), join( targets, ", " ), buildFile ) );
        }
        catch ( final Error e )
        {
            error = e;
            result.addError( new BuildException( "Error occurred in Ant build: %s\nTargets: [%s]\nBuild file: %s", e,
                                                 e.getMessage(), join( targets, ", " ), buildFile ) );
        }
        finally
        {
            project.fireBuildFinished( error );

            System.setIn( origIn );
            System.setOut( origOut );
            System.setErr( origErr );
        }
    }

}
