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

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.commonjava.sjbi.model.BuildContext;
import org.commonjava.sjbi.model.BuildResult;
import org.commonjava.sjbi.spi.BuildService;
import org.commonjava.sjbi.spi.SJBIMappingGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component( role = SJBI.class )
public class SJBI
{

    @Requirement( role = BuildService.class )
    private final Map<String, BuildService<?>> services = new HashMap<String, BuildService<?>>();

    public SJBI addService( final BuildService<?> service )
    {
        services.put( service.getId(), service );
        return this;
    }

    public BuildService<?> removeService( final BuildService<?> service )
    {
        return services.remove( service.getId() );
    }

    public BuildService<?> removeService( final String serviceId )
    {
        return services.remove( serviceId );
    }

    public synchronized SJBI setServices( final Map<String, BuildService<?>> services )
    {
        this.services.clear();
        this.services.putAll( services );
        return this;
    }

    public synchronized SJBI setServices( final BuildService<?>... services )
    {
        this.services.clear();
        for ( final BuildService<?> service : services )
        {
            this.services.put( service.getId(), service );
        }

        return this;
    }

    public boolean canBuild( final File projectDirectory )
    {
        for ( final BuildService<?> service : services.values() )
        {
            if ( service.canBuild( projectDirectory ) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean canBuild( final File projectDirectory, final String builderType )
    {
        return builderType != null && services.containsKey( builderType )
                        && services.get( builderType ).canBuild( projectDirectory );
    }

    public boolean canGenerateMappings( final File projectDirectory )
    {
        final String type = getBuilderType( projectDirectory );
        return canGenerateMappings( type );
    }

    public boolean canGenerateMappings( final String builderType )
    {
        if ( builderType != null )
        {
            return services.get( builderType ).getCapabilities().canGenerateMappings();
        }

        return false;
    }

    public File generateMappings( final File projectDirectory )
        throws SJBIException
    {
        final String type = getBuilderType( projectDirectory );
        return generateMappings( projectDirectory, type );
    }

    public File generateMappings( final File projectDirectory, final String builderType )
        throws SJBIException
    {
        checkBuilderType( projectDirectory, builderType );

        final SJBIMappingGenerator generator = services.get( builderType ).getCapabilities().getMappingGenerator();
        return generator.generateMappingsFile( projectDirectory );
    }

    public String getBuilderType( final File projectDirectory )
    {
        for ( final BuildService<?> service : services.values() )
        {
            if ( service.canBuild( projectDirectory ) )
            {
                return service.getId();
            }
        }

        return null;
    }

    public BuildContext newBuildContext( final File projectDirectory )
    {
        final String id = getBuilderType( projectDirectory );

        return newBuildContext( id );
    }

    public BuildContext newBuildContext( final String builderType )
    {
        return services.containsKey( builderType ) ? services.get( builderType ).newBuildContext() : null;
    }

    public BuildResult build( final BuildPhase endPhase, final File projectDirectory, final BuildContext context )
        throws SJBIException
    {
        final String id = getBuilderType( projectDirectory );
        return build( endPhase, projectDirectory, context, id );
    }

    @SuppressWarnings( "unchecked" )
    public <C extends BuildContext> BuildResult build( final BuildPhase endPhase, final File projectDirectory,
                                                       final C context, final String builderType )
        throws SJBIException
    {
        checkBuilderType( projectDirectory, builderType );

        final BuildService<C> support = (BuildService<C>) services.get( builderType );

        return support.build( endPhase, projectDirectory, context );
    }

    private void checkBuilderType( final File projectDirectory, final String builderType )
        throws SJBIException
    {
        if ( builderType == null || !services.containsKey( builderType ) )
        {
            throw new SJBIException( "No build support found for project directory: %s (type: %s)", projectDirectory,
                                     builderType );
        }

    }

}
