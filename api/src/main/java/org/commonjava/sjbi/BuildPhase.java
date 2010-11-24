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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum BuildPhase
{

    // NOTE: order matters here!
    CLEAN,
    PACKAGE,
    VERIFY,
    INSTALL,
    DEPLOY;

    public static Iterable<BuildPhase> startTo( final BuildPhase endPhase )
    {
        return values()[0].upTo( endPhase );
    }

    public Iterable<BuildPhase> upTo( final BuildPhase endPhase )
    {
        final int start = ordinal();
        final int end = endPhase.ordinal();

        if ( end < start )
        {
            return Collections.emptyList();
        }

        final BuildPhase[] phases = values();

        final List<BuildPhase> result = new ArrayList<BuildPhase>();
        for ( int i = start; i <= end; i++ )
        {
            result.add( phases[i] );
        }

        return result;
    }

}
