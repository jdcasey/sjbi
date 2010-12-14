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

package org.commonjava.sjbi.sant;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.model.BuildResult;
import org.commonjava.sjbi.spi.BuildCapabilities;
import org.commonjava.sjbi.spi.BuildMechanism;
import org.commonjava.sjbi.spi.BuildService;

import java.io.File;

@Component( role = BuildService.class, hint = SimpleAntService.ID )
public class SimpleAntService
    implements BuildService<SimpleAntContext>
{

    public static final String ID = "ant-simple";

    @Requirement( role = BuildMechanism.class, hint = SimpleAntService.ID )
    private SimpleAntMechanism builder;

    public SimpleAntService( final SimpleAntMechanism builder )
    {
        this.builder = builder;
    }

    SimpleAntService()
    {
    }

    void setBuilder( final SimpleAntMechanism builder )
    {
        this.builder = builder;
    }

    public String getId()
    {
        return ID;
    }

    public BuildCapabilities getCapabilities()
    {
        return SimpleAntCapabilities.INSTANCE;
    }

    public SimpleAntContext newBuildContext()
    {
        return new SimpleAntContext();
    }

    public boolean canBuild( final File projectDirectory )
    {
        final File project = new File( projectDirectory, "build.xml" );
        // final File mappings = new File( projectDirectory, AntMappingReader.FILENAME );

        return project.exists() && project.isFile(); // && mappings.exists() && mappings.isFile();
    }

    public BuildResult build( final BuildPhase endPhase, final File projectDirectory, final SimpleAntContext context )
        throws SJBIException
    {
        return builder.build( endPhase, projectDirectory, context );
    }

}
