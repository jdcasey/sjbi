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

package org.commonjava.sjbi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractBuildResult
    implements Iterable<ArtifactSetRef>, BuildResult
{

    private Collection<ArtifactSetRef> refs;

    private final Collection<Throwable> errors = new ArrayList<Throwable>();

    protected AbstractBuildResult()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.commonjava.sjbi.model.BuildResult#getErrors()
     */
    public Collection<Throwable> getErrors()
    {
        return errors;
    }

    protected BuildResult setArtifactSets( final Collection<ArtifactSetRef> artifactSets )
    {
        if ( artifactSets != null && !artifactSets.isEmpty() )
        {
            refs = new ArrayList<ArtifactSetRef>( artifactSets );
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.commonjava.sjbi.model.BuildResult#getArtifactSets()
     */
    public Collection<ArtifactSetRef> getArtifactSets()
    {
        return refs;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.commonjava.sjbi.model.BuildResult#iterator()
     */
    public Iterator<ArtifactSetRef> iterator()
    {
        return refs.iterator();
    }

    protected synchronized BuildResult setErrors( final Collection<Throwable> errors )
    {
        if ( errors != null && !errors.isEmpty() )
        {
            this.errors.clear();
            this.errors.addAll( errors );
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.commonjava.sjbi.model.BuildResult#addError(java.lang.Throwable)
     */
    public BuildResult addError( final Throwable error )
    {
        errors.add( error );
        return this;
    }

}
