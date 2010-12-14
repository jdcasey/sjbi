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

package org.commonjava.sjbi.spi;

import org.commonjava.sjbi.BuildPhase;
import org.commonjava.sjbi.SJBIException;
import org.commonjava.sjbi.model.BuildContext;
import org.commonjava.sjbi.model.BuildResult;

import java.io.File;

public interface BuildService<C extends BuildContext>
{

    String getId();

    boolean canBuild( File projectDirectory );

    C newBuildContext();

    BuildCapabilities getCapabilities();

    BuildResult build( BuildPhase endPhase, File projectDirectory, C context )
        throws SJBIException;

}
