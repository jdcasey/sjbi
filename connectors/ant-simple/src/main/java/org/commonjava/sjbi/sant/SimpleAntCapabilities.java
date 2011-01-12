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

import org.commonjava.sjbi.sant.mapping.AntMappingWriter;
import org.commonjava.sjbi.spi.BuildCapabilities;
import org.commonjava.sjbi.spi.SJBIMappingGenerator;

public class SimpleAntCapabilities
    implements BuildCapabilities
{

    public static final SimpleAntCapabilities INSTANCE = new SimpleAntCapabilities();

    private SimpleAntCapabilities()
    {
    }

    public boolean canGenerateMappings()
    {
        return true;
    }

    public boolean usesAutoChainedWorkflow()
    {
        return true;
    }

    public SJBIMappingGenerator getMappingGenerator()
    {
        return new AntMappingWriter();
    }

}
