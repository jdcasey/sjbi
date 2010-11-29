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

import org.commonjava.sjbi.BuildPhase;

public enum M3PhaseMapping
{
    CLEAN( BuildPhase.CLEAN, "clean" ),
    PACKAGE( BuildPhase.PACKAGE, "package" ),
    VERIFY( BuildPhase.VERIFY, "verify" ),
    INSTALL( BuildPhase.INSTALL, "install" ),
    DEPLOY( BuildPhase.DEPLOY, "deploy" );

    private BuildPhase phase;

    private String m3Phase;

    private M3PhaseMapping( final BuildPhase phase, final String m3Phase )
    {
        this.phase = phase;
        this.m3Phase = m3Phase;
    }

    public String m3Phase()
    {
        return m3Phase;
    }

    public BuildPhase phase()
    {
        return phase;
    }

    public static M3PhaseMapping mappingFor( final BuildPhase phase )
    {
        for ( final M3PhaseMapping m : values() )
        {
            if ( m.phase == phase )
            {
                return m;
            }
        }

        return null;
    }

}
