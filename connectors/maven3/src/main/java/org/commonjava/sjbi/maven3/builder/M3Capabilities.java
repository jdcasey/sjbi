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

import org.commonjava.sjbi.spi.BuildCapabilities;
import org.commonjava.sjbi.spi.SJBIMappingGenerator;

public final class M3Capabilities
    implements BuildCapabilities
{

    public static final M3Capabilities INSTANCE = new M3Capabilities();

    private M3Capabilities()
    {
    }

    public boolean canGenerateMappings()
    {
        return false;
    }

    public boolean usesAutoChainedWorkflow()
    {
        return true;
    }

    public SJBIMappingGenerator getMappingGenerator()
    {
        return SJBIMappingGenerator.NOOP;
    }

}
