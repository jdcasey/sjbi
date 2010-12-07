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

package org.commonjava.sjbi.validation;

import org.codehaus.plexus.component.annotations.Component;
import org.commonjava.sjbi.builder.BuildException;
import org.commonjava.sjbi.builder.BuildResult;
import org.commonjava.sjbi.model.ArtifactRef;
import org.commonjava.sjbi.model.ArtifactSetRef;
import org.commonjava.sjbi.model.ProjectRef;

import java.io.File;

@Component( role = ArtifactSetValidator.class, hint = "file-existence" )
public class FileExistenceValidator
    implements ArtifactSetValidator
{

    public boolean isValid( final ArtifactSetRef ars, final BuildResult result )
    {
        final ProjectRef pr = ars.getProjectRef();

        boolean valid = true;
        final File pom = pr.getPomFile();
        if ( pom == null || !pom.exists() || !pom.isFile() )
        {
            result.addError( new BuildException( "Invalid POM: %s\nIn: %s", pom, pr.coordKey() ) );
            valid = false;
        }

        for ( final ArtifactRef ref : ars.getArtifactsBySubKey().values() )
        {
            final File artifact = ref.getArtifactFile();
            if ( artifact == null || !artifact.exists() || !artifact.isFile() )
            {
                String classifier = ref.getClassifier();
                if ( classifier == null )
                {
                    classifier = "-NONE-";
                }

                result.addError( new BuildException( "Invalid artifact file: %s\n\nType: %s\nClassifier: %s\nIn: %s",
                                                     pom, ref.getType(), classifier, pr.coordKey() ) );
                valid = false;
            }
        }

        return valid;
    }

}
