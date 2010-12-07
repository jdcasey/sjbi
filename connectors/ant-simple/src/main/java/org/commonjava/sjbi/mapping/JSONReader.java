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

package org.commonjava.sjbi.mapping;

import static org.apache.commons.io.IOUtils.closeQuietly;

import org.commonjava.sjbi.SimpleAntContext;
import org.commonjava.sjbi.builder.BuildException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader
    implements AntMappingReader
{

    public static final String FILENAME = "sjbi.json";

    public AntMapping readMapping( final File projectDirectory, final SimpleAntContext context )
        throws BuildException
    {
        final File f = new File( projectDirectory, FILENAME );
        if ( !f.exists() || !f.isFile() )
        {
            return null;
        }

        FileReader in = null;
        try
        {
            in = new FileReader( f );

            final Gson gson = new GsonBuilder().create();
            return gson.fromJson( in, AntMapping.class );
        }
        catch ( final IOException e )
        {
            throw new BuildException( "Cannot read build-interface mappings from: %s. Reason: %s", e, FILENAME,
                                      e.getMessage() );
        }
        finally
        {
            closeQuietly( in );
        }
    }

}
