package net.sumaris.core.dao.data;

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

import net.sumaris.core.dao.technical.SortDirection;
import net.sumaris.core.model.data.Trip;
import net.sumaris.core.vo.data.MeasurementVO;
import net.sumaris.core.vo.data.TripVO;
import net.sumaris.core.vo.filter.TripFilterVO;

import java.util.List;

public interface TripDao {

    List<TripVO> getAllTrips(int offset, int size, String sortAttribute, SortDirection sortDirection);

    List<TripVO> findByFilter(TripFilterVO filter, int offset, int size, String sortAttribute, SortDirection sortDirection);

    TripVO get(int id);

    <T> T get(int id, Class<T> targetClass);

    void delete(int id);

    TripVO save(TripVO trip);

    TripVO toTripVO(Trip trip);

}
