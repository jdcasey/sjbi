package org.commonjava.sjbi.cli;

import org.apache.log4j.Logger;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.commonjava.emb.EMBException;
import org.commonjava.emb.app.AbstractEMBApplication;
import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBI;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.model.BuildContext;
import org.commonjava.sjbi.model.BuildResult;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component( role = Cli.class )
public class Cli
    extends AbstractEMBApplication
{

    private static final Logger LOGGER = Logger.getLogger( Cli.class );

    public static void main( final String[] args )
        throws SJBIException
    {
        final Cli cli = new Cli();
        final CmdLineParser parser = new CmdLineParser( cli );
        try
        {
            parser.parseArgument( args );

            final BuildResult result = cli.build();
            if ( result != null )
            {
                log( result );
            }

            if ( cli.displayUsage )
            {
                printUsage( parser, null );
            }
        }
        catch ( final CmdLineException e )
        {
            printUsage( parser, e );
        }
    }

    private static void log( final BuildResult result )
    {
        if ( result == null )
        {
            return;
        }

        // final Collection<Throwable> errors = result.getErrors();
        // final Collection<ArtifactSetRef> sets = result.getArtifactSets();
    }

    @Option( name = "-S", usage = "Check whether SJBI can build the project." )
    private boolean checkingSanity = false;

    @Option( name = "-d", usage = "Project directory to build." )
    private File projectDirectory = new File( System.getProperty( "user.dir", "." ) );

    @Option( name = "-D", usage = "Set a property for the build. Format: -Dkey=value", multiValued = true )
    private Map<String, String> properties = new HashMap<String, String>();

    @Argument( usage = "Build up to this phase." )
    private BuildPhase buildTo = BuildPhase.VERIFY;

    @Option( name = "-h", aliases = { "--help" }, usage = "Display this help message." )
    private boolean displayUsage = false;

    @Option( name = "-G", aliases = { "--generate-mappings" }, usage = "Generate a sample mappings file for the project, if necessary / possible." )
    private boolean generateMappings = false;

    @Requirement
    private transient SJBI sjbi;

    public Cli()
    {
    }

    public Cli( final SJBI sjbi )
    {
        this.sjbi = sjbi;
    }

    public BuildResult build()
        throws SJBIException
    {
        ensureStarted();

        if ( displayUsage )
        {
            return null;
        }

        try
        {
            projectDirectory = projectDirectory.getCanonicalFile();
        }
        catch ( final IOException e )
        {
            projectDirectory = projectDirectory.getAbsoluteFile();
        }

        final String type = sjbi.getBuilderType( projectDirectory );
        if ( type == null )
        {
            LOGGER.error( "Cannot find SJBI builder for project: " + projectDirectory );
        }
        else if ( checkingSanity )
        {
            LOGGER.info( "SJBI builder: '" + type + "' will handle project: " + projectDirectory );
            return null;
        }
        else if ( generateMappings )
        {
            if ( sjbi.canGenerateMappings( type ) )
            {
                final File mappingsFile = sjbi.generateMappings( projectDirectory, type );
                LOGGER.info( "Generated mappings-file: " + mappingsFile );
            }

            return null;
        }

        final BuildContext context = sjbi.newBuildContext( type );
        context.setProperties( properties );

        return sjbi.build( buildTo, projectDirectory, context, type );
    }

    private synchronized void ensureStarted()
        throws SJBIException
    {
        if ( sjbi == null )
        {
            try
            {
                load();
            }
            catch ( final EMBException e )
            {
                throw new SJBIException( "Failed to start SJBI: %s", e, e.getMessage() );
            }
        }
    }

    private static void printUsage( final CmdLineParser parser, final CmdLineException error )
    {
        if ( error != null )
        {
            System.err.println( "Invalid option(s): " + error.getMessage() );
            System.err.println();
        }

        parser.printUsage( System.err );
        System.err.println( "Usage: $0 [OPTIONS] <phase>" );
        System.err.println();
        System.err.println();
        System.err.println( parser.printExample( ExampleMode.ALL ) );
        System.err.println();
    }

    public boolean isCheckingSanity()
    {
        return checkingSanity;
    }

    public void setCheckingSanity( final boolean checkingSanity )
    {
        this.checkingSanity = checkingSanity;
    }

    public File getProjectDirectory()
    {
        return projectDirectory;
    }

    public void setProjectDirectory( final File projectDirectory )
    {
        this.projectDirectory = projectDirectory;
    }

    public String getId()
    {
        return "sjbi";
    }

    public String getName()
    {
        return "SJBI.Cli";
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties( final Map<String, String> properties )
    {
        this.properties = properties;
    }

    public BuildPhase getBuildTo()
    {
        return buildTo;
    }

    public void setBuildTo( final BuildPhase buildTo )
    {
        this.buildTo = buildTo;
    }

    public boolean isDisplayHelp()
    {
        return displayUsage;
    }

    public void setDisplayHelp( final boolean displayHelp )
    {
        displayUsage = displayHelp;
    }

    public boolean isGenerateMappings()
    {
        return generateMappings;
    }

    public void setGenerateMappings( final boolean generateMappings )
    {
        this.generateMappings = generateMappings;
    }
}
