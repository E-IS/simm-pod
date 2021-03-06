package net.sumaris.core.service.referential.taxon;

/*-
 * #%L
 * SUMARiS:: Core
 * %%
 * Copyright (C) 2018 - 2019 SUMARiS Consortium
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

import net.sumaris.core.dao.referential.taxon.TaxonGroupRepository;
import net.sumaris.core.dao.schema.DatabaseSchemaDao;
import net.sumaris.core.dao.schema.event.DatabaseSchemaListener;
import net.sumaris.core.dao.schema.event.SchemaUpdatedEvent;
import net.sumaris.core.dao.technical.SortDirection;
import net.sumaris.core.exception.VersionNotFoundException;
import net.sumaris.core.vo.filter.ReferentialFilterVO;
import net.sumaris.core.vo.referential.TaxonGroupVO;
import org.nuiton.version.Version;
import org.nuiton.version.VersionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service("taxonGroupService")
public class TaxonGroupServiceImpl implements TaxonGroupService, DatabaseSchemaListener {

    private static final Logger log = LoggerFactory.getLogger(TaxonGroupServiceImpl.class);

    @Autowired
    protected TaxonGroupRepository taxonGroupRepository;

    @Autowired
    protected TaxonGroupService self;

    @Autowired
    protected DatabaseSchemaDao databaseSchemaDao;

    @PostConstruct
    protected void init() {
        databaseSchemaDao.addListener(this);
    }

    @Override
    public void onSchemaUpdated(SchemaUpdatedEvent event) {
        updateTaxonGroupHierarchies();
    }

    @Override
    public boolean updateTaxonGroupHierarchies() {

        try {
            // Check version
            Version dbVersion = databaseSchemaDao.getSchemaVersion();
            Version minVersion = VersionBuilder.create("0.15.0").build();

            if (dbVersion == null) {
                log.info("/!\\ Skipping taxon group hierarchy update, because database schema version is unknown. Waiting schema update...");
                return false;
            } else if (dbVersion.before(minVersion)) {
                log.info("/!\\ Skipping taxon group hierarchy update, because database schema version < 0.15.0. Waiting schema update...");
                return false;
            }
        }
        catch(VersionNotFoundException e) {
            // ok continue (schema seems to be new)
        }

        taxonGroupRepository.updateTaxonGroupHierarchies();
        return true;
    }

    @Override
    public List<TaxonGroupVO> findTargetSpeciesByFilter(ReferentialFilterVO filter,
                                           int offset,
                                           int size,
                                           String sortAttribute,
                                           SortDirection sortDirection) {
        return taxonGroupRepository.findTargetSpeciesByFilter(filter, offset, size, sortAttribute, sortDirection);
    }

    @Override
    public List<Integer> getAllIdByReferenceTaxonId(int referenceTaxonId, Date startDate, Date endDate) {
        return taxonGroupRepository.getAllIdByReferenceTaxonId(referenceTaxonId, startDate, endDate);
    }
}
