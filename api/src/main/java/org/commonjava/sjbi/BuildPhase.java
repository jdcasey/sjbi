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

    public BuildPhase previousNotClean()
    {
        if ( ordinal() == 1 )
        {
            return null;
        }

        return values()[ordinal() - 1];
    }

    public static String listing()
    {
        final StringBuilder buf = new StringBuilder();

        buf.append( "[" );

        boolean first = true;
        for ( final BuildPhase phase : values() )
        {
            if ( first )
            {
                first = false;
            }
            else
            {
                buf.append( ", " );
            }

            buf.append( phase.name() );
        }
        buf.append( "]" );

        return buf.toString();
    }

}
