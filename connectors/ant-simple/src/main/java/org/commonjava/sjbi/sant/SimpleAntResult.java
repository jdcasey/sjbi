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

import org.commonjava.sjbi.model.ArtifactSetRef;
import org.commonjava.sjbi.model.AbstractBuildResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleAntResult
    extends AbstractBuildResult
{

    private final List<Throwable> errors = new ArrayList<Throwable>();

    private Collection<ArtifactSetRef> artifactSets;

    SimpleAntResult()
    {
    }

    SimpleAntResult( final SimpleAntResult initialResult, final Collection<ArtifactSetRef> refs )
    {
        errors.addAll( initialResult.getErrors() );
        artifactSets =
            refs == null || refs.isEmpty() ? new ArrayList<ArtifactSetRef>() : new ArrayList<ArtifactSetRef>( refs );
    }

    @Override
    public SimpleAntResult addError( final Throwable error )
    {
        errors.add( error );
        return this;
    }

    @Override
    public Collection<Throwable> getErrors()
    {
        return errors;
    }

    @Override
    public Collection<ArtifactSetRef> getArtifactSets()
    {
        return artifactSets;
    }

}
