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

import java.util.Map;

public class AntProjectMetadata
{

    private String pom;

    private String coord;

    private Map<String, String> artifacts;

    public String getPom()
    {
        return pom;
    }

    public void setPom( final String pom )
    {
        this.pom = pom;
    }

    public String getCoord()
    {
        return coord;
    }

    public void setCoord( final String coord )
    {
        this.coord = coord;
    }

    public Map<String, String> getArtifacts()
    {
        return artifacts;
    }

    public void setArtifacts( final Map<String, String> artifacts )
    {
        this.artifacts = artifacts;
    }

}
