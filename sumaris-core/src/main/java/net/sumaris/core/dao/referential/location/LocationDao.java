package net.sumaris.core.dao.referential.location;

/*-
 * #%L
 * SUMARiS:: Core
 * %%
 * Copyright (C) 2018 SUMARiS Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import net.sumaris.core.model.referential.location.Location;
import net.sumaris.core.vo.referential.LocationVO;

import java.util.List;

public interface LocationDao {

    List<LocationVO> getByLocationLevel(int locationLevelId);

    LocationVO findByLabel(String label);

    LocationVO getByLabel(String label);

    Location create(Location location);

    Location update(Location location);

    LocationVO toLocationVO(Location source);

    boolean hasAssociation(int childLocationId, int parentLocationId);

    void addAssociation(int childLocationId, int parentLocationId, double childSurfaceRatio);

    /**
     * Update technical table LOCATION_HIERARCHY, from child/parent links found in LOCATION
     */
    void updateLocationHierarchy();

    Location get(int id);
}
