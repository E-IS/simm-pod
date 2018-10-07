package net.sumaris.core.dao.data.batch;

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

import com.google.common.base.Preconditions;
import net.sumaris.core.dao.referential.ReferentialDao;
import net.sumaris.core.dao.technical.Beans;
import net.sumaris.core.dao.technical.hibernate.HibernateDaoSupport;
import net.sumaris.core.model.administration.programStrategy.PmfmStrategy;
import net.sumaris.core.model.administration.user.Department;
import net.sumaris.core.model.data.Operation;
import net.sumaris.core.model.data.batch.Batch;
import net.sumaris.core.model.data.sample.Sample;
import net.sumaris.core.model.referential.QualityFlag;
import net.sumaris.core.model.referential.taxon.TaxonGroup;
import net.sumaris.core.vo.administration.user.DepartmentVO;
import net.sumaris.core.vo.data.BatchVO;
import net.sumaris.core.vo.data.OperationVO;
import net.sumaris.core.vo.data.SampleVO;
import net.sumaris.core.vo.referential.ReferentialVO;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("batchDao")
public class BatchDaoImpl extends HibernateDaoSupport implements BatchDao {

    /** Logger. */
    private static final Logger log =
            LoggerFactory.getLogger(BatchDaoImpl.class);

    @Autowired
    private ReferentialDao referentialDao;

    @Override
    public List<BatchVO> getAllByOperationId(int operationId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Batch> query = cb.createQuery(Batch.class);
        Root<Batch> root = query.from(Batch.class);

        query.select(root);

        ParameterExpression<Integer> tripIdParam = cb.parameter(Integer.class);

        query.where(cb.equal(root.get(Batch.PROPERTY_OPERATION).get(Batch.PROPERTY_ID), tripIdParam));

        // Sort by rank order
        query.orderBy(cb.asc(root.get(PmfmStrategy.PROPERTY_RANK_ORDER)));

        return toBatchVOs(getEntityManager().createQuery(query)
                .setParameter(tripIdParam, operationId).getResultList(), false);
    }

    @Override
    public BatchVO get(int id) {
        Batch entity = get(Batch.class, id);
        return toBatchVO(entity, false);
    }

    @Override
    public List<BatchVO> saveByOperationId(int operationId, List<BatchVO> sources) {

        // Load parent entity
        Operation parent = get(Operation.class, operationId);

        // Remember existing entities
        final List<Integer> sourcesIdsToRemove = Beans.collectIds(Beans.getList(parent.getBatches()));

        // Save each gears
        List<BatchVO> result = sources.stream().map(source -> {
            source.setOperationId(operationId);
            if (source.getId() != null) {
                sourcesIdsToRemove.remove(source.getId());
            }
            return save(source);
        }).collect(Collectors.toList());

        // Remove unused entities
        if (CollectionUtils.isNotEmpty(sourcesIdsToRemove)) {
            sourcesIdsToRemove.forEach(this::delete);
        }

        return result;
    }


    @Override
    public BatchVO save(BatchVO source) {
        Preconditions.checkNotNull(source);

        EntityManager entityManager = getEntityManager();
        Batch entity = null;
        if (source.getId() != null) {
            entity = get(Batch.class, source.getId());
        }
        boolean isNew = (entity == null);
        if (isNew) {
            entity = new Batch();
        }

        if (!isNew) {
            // Check update date
            // FIXME: Cliant app: update entity from the save() result
            //checkUpdateDateForUpdate(source, entity);

            // Lock entityName
            //lockForUpdate(entity);
        }

        // Copy some fields from the trip
        copySomeFieldsFromOperation(source);

        // VO -> Entity
        batchVOToEntity(source, entity, true);

        // Update update_dt
        Timestamp newUpdateDate = getDatabaseCurrentTimestamp();
        entity.setUpdateDate(newUpdateDate);

        // Save entityName
        if (isNew) {
            entityManager.persist(entity);
            source.setId(entity.getId());
        } else {
            entityManager.merge(entity);
        }

        source.setUpdateDate(newUpdateDate);

        entityManager.flush();
        entityManager.clear();

        return source;
    }

    @Override
    public void delete(int id) {

        log.debug(String.format("Deleting batch {id=%s}...", id));
        delete(Batch.class, id);
    }

    @Override
    public BatchVO toBatchVO(Batch source) {
        return toBatchVO(source, true);
    }


    /* -- protected methods -- */

    protected BatchVO toBatchVO(Batch source, boolean allFields) {

        if (source == null) return null;

        BatchVO target = new BatchVO();

        Beans.copyProperties(source, target);

        // Taxon group
        if (source.getTaxonGroup() != null) {
            ReferentialVO taxonGroup = referentialDao.toReferentialVO(source.getTaxonGroup());
            target.setTaxonGroup(taxonGroup);
        }

        // Operation
        if (source.getOperation() != null) {
            target.setOperationId(source.getOperation().getId());
        }

        // Parent batch
        if (source.getParent() != null) {
            target.setParentId(source.getParent().getId());
        }

        // If full export
        if (allFields) {
            // Recorder department
            DepartmentVO recorderDepartment = referentialDao.toTypedVO(source.getRecorderDepartment(), DepartmentVO.class);
            target.setRecorderDepartment(recorderDepartment);
        }

        return target;
    }

    protected void copySomeFieldsFromOperation(BatchVO target) {
        OperationVO source = target.getOperation();
        if (source == null) return;

        target.setRecorderDepartment(source.getRecorderDepartment());

    }

    protected List<BatchVO> toBatchVOs(List<Batch> source, boolean allFields) {
        return this.toBatchVOs(source.stream(), allFields);
    }

    protected List<BatchVO> toBatchVOs(Stream<Batch> source, boolean allFields) {
        return source.map(s -> this.toBatchVO(s, allFields))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected void batchVOToEntity(BatchVO source, Batch target, boolean copyIfNull) {

        Beans.copyProperties(source, target);

        // Taxon group
        if (copyIfNull || source.getTaxonGroup() != null) {
            if (source.getTaxonGroup() == null || source.getTaxonGroup().getId() == null) {
                target.setTaxonGroup(null);
            }
            else {
                target.setTaxonGroup(load(TaxonGroup.class, source.getTaxonGroup().getId()));
            }
        }

        // Operation
        Integer opeId = source.getOperationId() != null ? source.getOperationId() : (source.getOperation() != null ? source.getOperation().getId() : null);
        if (copyIfNull || (opeId != null)) {
            if (opeId == null) {
                target.setOperation(null);
            }
            else {
                target.setOperation(load(Operation.class, opeId));
            }
        }

        // Recorder department
        if (copyIfNull || source.getRecorderDepartment() != null) {
            if (source.getRecorderDepartment() == null || source.getRecorderDepartment().getId() == null) {
                target.setRecorderDepartment(null);
            }
            else {
                target.setRecorderDepartment(load(Department.class, source.getRecorderDepartment().getId()));
            }
        }

        // Quality flag
        if (copyIfNull || source.getQualityFlagId() != null) {
            if (source.getQualityFlagId() == null) {
                target.setQualityFlag(load(QualityFlag.class, config.getDefaultQualityFlagId()));
            }
            else {
                target.setQualityFlag(load(QualityFlag.class, source.getQualityFlagId()));
            }
        }
    }
}
