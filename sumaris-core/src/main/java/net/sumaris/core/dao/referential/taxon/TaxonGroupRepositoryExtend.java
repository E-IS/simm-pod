package net.sumaris.core.dao.referential.taxon;

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
import net.sumaris.core.model.referential.taxon.TaxonGroup;
import net.sumaris.core.vo.filter.ReferentialFilterVO;
import net.sumaris.core.vo.referential.ReferentialVO;
import net.sumaris.core.vo.referential.TaxonGroupVO;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

@NoRepositoryBean
public interface TaxonGroupRepositoryExtend {

    void updateTaxonGroupHierarchies();

    void updateTaxonGroupHierarchy();

    void updateTaxonGroup2TaxonHierarchy();

    long countTaxonGroupHierarchy();

    TaxonGroupVO toTaxonGroupVO(TaxonGroup taxonGroup);

    List<TaxonGroupVO> findTargetSpeciesByFilter(
            ReferentialFilterVO filter,
            int offset,
            int size,
            String sortAttribute,
            SortDirection sortDirection);

    List<ReferentialVO> getAllDressingByTaxonGroupId(int taxonGroupId, Date startDate, Date endDate, int locationId);

    List<ReferentialVO> getAllPreservingByTaxonGroupId(int taxonGroupId, Date startDate, Date endDate, int locationId);

    List<Integer> getAllIdByReferenceTaxonId(int referenceTaxonId, Date startDate, Date endDate);
}
