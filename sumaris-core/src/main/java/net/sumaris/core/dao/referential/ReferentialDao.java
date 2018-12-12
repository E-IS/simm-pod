package net.sumaris.core.dao.referential;

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

import net.sumaris.core.dao.cache.CacheNames;
import net.sumaris.core.dao.technical.SortDirection;
import net.sumaris.core.model.referential.IReferentialEntity;
import net.sumaris.core.vo.filter.ReferentialFilterVO;
import net.sumaris.core.vo.referential.IReferentialVO;
import net.sumaris.core.vo.referential.ReferentialTypeVO;
import net.sumaris.core.vo.referential.ReferentialVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;

public interface ReferentialDao {


    @Cacheable(cacheNames = CacheNames.REFERENTIAL_TYPES)
    List<ReferentialTypeVO> getAllTypes();

    List<ReferentialVO> getAllLevels(String entityName);

    ReferentialVO getLevelById(String entityName, int levelId);

    List<ReferentialVO> findByFilter(String entityName,
                                     ReferentialFilterVO filter,
                                     int offset,
                                     int size,
                                     String sortAttribute,
                                     SortDirection sortDirection);

    ReferentialVO findByUniqueLabel(String entityName, String label);

    <T extends IReferentialEntity> ReferentialVO toReferentialVO(T source);

    <T extends IReferentialVO, S extends IReferentialEntity> T toTypedVO(S source, Class<T> targetClazz);

    ReferentialVO save(ReferentialVO source);

    @Caching(evict= {
            @CacheEvict(cacheNames = CacheNames.PERSON_BY_ID, key = "#id", condition = "#entityName == 'Person'"),
            @CacheEvict(cacheNames = CacheNames.DEPARTMENT_BY_ID, key = "#id", condition = "#entityName == 'Department'"),
            @CacheEvict(cacheNames = CacheNames.PMFM_BY_ID, key = "#id", condition = "#entityName == 'Pmfm'"),
            @CacheEvict(cacheNames = CacheNames.PROGRAM_BY_LABEL, key = "#id", condition = "#entityName == 'Program'")
    })
    void delete(String entityName, int id);

    Long count(String entityName);
}
