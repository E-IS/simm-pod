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
import net.sumaris.core.model.data.ObservedLocation;
import net.sumaris.core.vo.data.DataFetchOptions;
import net.sumaris.core.vo.data.ObservedLocationVO;
import net.sumaris.core.vo.filter.ObservedLocationFilterVO;

import java.util.List;

public interface ObservedLocationDao {

    List<ObservedLocationVO> getAll(int offset, int size, String sortAttribute, SortDirection sortDirection, DataFetchOptions fetchOptions);

    List<ObservedLocationVO> findByFilter(ObservedLocationFilterVO filter, int offset, int size, String sortAttribute,
                                          SortDirection sortDirection, DataFetchOptions fetchOptions);

    Long countByFilter(ObservedLocationFilterVO filter);

    ObservedLocationVO get(int id);

    <T> T get(int id, Class<T> targetClass);

    void delete(int id);

    ObservedLocationVO save(ObservedLocationVO observedLocation);

    ObservedLocationVO toObservedLocationVO(ObservedLocation observedLocation);

    ObservedLocationVO control(ObservedLocationVO observedLocation);

    ObservedLocationVO validate(ObservedLocationVO observedLocation);

    ObservedLocationVO unvalidate(ObservedLocationVO observedLocation);
}