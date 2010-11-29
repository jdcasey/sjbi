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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.commonjava.sjbi.builder.BuildResult;
import org.commonjava.sjbi.model.ArtifactSetRef;
import org.commonjava.sjbi.model.ProjectRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class M3BuildResult
    implements BuildResult, Iterable<ArtifactSetRef>
{

    private final Collection<ArtifactSetRef> refs;

    private final Collection<Throwable> errors;

    public M3BuildResult( final MavenExecutionResult mavenResult )
    {
        errors = Collections.unmodifiableCollection( mavenResult.getExceptions() );

        final List<ArtifactSetRef> ars = new ArrayList<ArtifactSetRef>();
        for ( final MavenProject project : mavenResult.getTopologicallySortedProjects() )
        {
            final ProjectRef pr = new ProjectRef( project.getGroupId(), project.getArtifactId(), project.getVersion() );
            pr.setPomFile( project.getFile() );

            final ArtifactSetRef ar = new ArtifactSetRef( pr );

            final Artifact mainArtifact = project.getArtifact();
            ar.addArtifactRef( mainArtifact.getType(), mainArtifact.getClassifier(), mainArtifact.getFile() );

            for ( final Artifact a : project.getAttachedArtifacts() )
            {
                ar.addArtifactRef( a.getType(), a.getClassifier(), a.getFile() );
            }

            ars.add( ar );
        }

        refs = Collections.unmodifiableCollection( ars );
    }

    public Collection<Throwable> getErrors()
    {
        return errors;
    }

    public Collection<ArtifactSetRef> getArtifactSets()
    {
        return refs;
    }

    public Iterator<ArtifactSetRef> iterator()
    {
        return refs.iterator();
    }

}
