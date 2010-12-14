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

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.model.BuildResult;
import org.commonjava.sjbi.spi.BuildCapabilities;
import org.commonjava.sjbi.spi.BuildMechanism;
import org.commonjava.sjbi.spi.BuildService;

import javax.inject.Inject;

import java.io.File;

@Component( role = BuildService.class, hint = Maven3Service.ID )
public class Maven3Service
    implements BuildService<M3Context>
{

    public static final String ID = "maven3";

    @Requirement( role = BuildMechanism.class, hint = Maven3Service.ID )
    private final M3Mechanism builder;

    @Inject
    public Maven3Service( final M3Mechanism builder )
    {
        this.builder = builder;
    }

    public String getId()
    {
        return ID;
    }

    public BuildCapabilities getCapabilities()
    {
        return M3Capabilities.INSTANCE;
    }

    public boolean canBuild( final File projectDirectory )
    {
        final File pom = new File( projectDirectory, "pom.xml" );
        return pom.exists() && pom.isFile();
    }

    public M3Context newBuildContext()
    {
        return new M3Context();
    }

    public BuildResult build( final BuildPhase endPhase, final File projectDirectory, final M3Context context )
        throws SJBIException
    {
        return builder.build( endPhase, projectDirectory, context );
    }

}
