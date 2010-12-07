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

import org.commonjava.sjbi.builder.BuildResult;
import org.commonjava.sjbi.model.ArtifactSetRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleAntResult
    implements BuildResult
{

    private final List<Throwable> errors = new ArrayList<Throwable>();

    private Collection<ArtifactSetRef> artifactSets;

    public SimpleAntResult addError( final Throwable error )
    {
        errors.add( error );
        return this;
    }

    public Collection<Throwable> getErrors()
    {
        return errors;
    }

    public Collection<ArtifactSetRef> getArtifactSets()
    {
        return artifactSets;
    }

    BuildResult setArtifactSets( final Collection<ArtifactSetRef> artifactSets )
    {
        this.artifactSets = artifactSets;
        return this;
    }

}
