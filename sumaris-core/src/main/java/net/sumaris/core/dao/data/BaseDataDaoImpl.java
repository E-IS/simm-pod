package net.sumaris.core.dao.data;

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

import com.google.common.collect.Sets;
import net.sumaris.core.config.SumarisConfiguration;
import net.sumaris.core.dao.technical.SortDirection;
import net.sumaris.core.dao.technical.hibernate.HibernateDaoSupport;
import net.sumaris.core.dao.technical.model.IEntity;
import net.sumaris.core.exception.SumarisTechnicalException;
import net.sumaris.core.model.administration.programStrategy.Program;
import net.sumaris.core.model.administration.user.Department;
import net.sumaris.core.model.administration.user.Person;
import net.sumaris.core.model.data.*;
import net.sumaris.core.model.referential.QualityFlag;
import net.sumaris.core.util.Beans;
import net.sumaris.core.vo.administration.user.DepartmentVO;
import net.sumaris.core.vo.administration.user.PersonVO;
import net.sumaris.core.vo.data.IDataVO;
import net.sumaris.core.vo.data.IRootDataVO;
import net.sumaris.core.vo.data.VesselSnapshotVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>*
 */
public abstract class BaseDataDaoImpl extends HibernateDaoSupport {

    public <T extends Serializable> void copyRootDataProperties(IRootDataVO<T> source,
                                                                IRootDataEntity<T> target,
                                                                boolean copyIfNull) {

        // Copy root data properties without exception
        copyRootDataProperties(source, target, copyIfNull, (String) null);
    }

    public <T extends Serializable> void copyRootDataProperties(IRootDataVO<T> source,
                                                                IRootDataEntity<T> target,
                                                                boolean copyIfNull,
                                                                String... exceptProperties) {

        // Copy data properties except some properties if specified
        copyDataProperties(source, target, copyIfNull, exceptProperties);

        // Recorder person
        copyRecorderPerson(source, target, copyIfNull);

        // Program
        copyProgram(source, target, copyIfNull);
    }

    public <T extends Serializable> void copyDataProperties(IDataVO<T> source,
                                                            IDataEntity<T> target,
                                                            boolean copyIfNull) {

        // Copy data properties without exception
        copyDataProperties(source, target, copyIfNull, (String) null);
    }

    public <T extends Serializable> void copyDataProperties(IDataVO<T> source,
                                                            IDataEntity<T> target,
                                                            boolean copyIfNull,
                                                            String... exceptProperties) {

        // Copy bean properties except some properties if specified
        Beans.copyProperties(source, target, exceptProperties);

        // Recorder department
        copyRecorderDepartment(source, target, copyIfNull);

        // Quality flag
        copyQualityFlag(source, target, copyIfNull);
    }

    public <T extends Serializable> void copyRecorderDepartment(IWithRecorderDepartmentEntity<T, DepartmentVO> source,
                                                                IWithRecorderDepartmentEntity<T, Department> target,
                                                                boolean copyIfNull) {
        // Recorder department
        if (copyIfNull || source.getRecorderDepartment() != null) {
            if (source.getRecorderDepartment() == null || source.getRecorderDepartment().getId() == null) {
                target.setRecorderDepartment(null);
            } else {
                target.setRecorderDepartment(load(Department.class, source.getRecorderDepartment().getId()));
            }
        }
    }

    public <T extends Serializable> void copyRecorderPerson(IWithRecorderPersonEntity<T, PersonVO> source,
                                                            IWithRecorderPersonEntity<T, Person> target,
                                                            boolean copyIfNull) {
        if (copyIfNull || source.getRecorderPerson() != null) {
            if (source.getRecorderPerson() == null || source.getRecorderPerson().getId() == null) {
                target.setRecorderPerson(null);
            } else {
                target.setRecorderPerson(load(Person.class, source.getRecorderPerson().getId()));
            }
        }
    }

    public <T extends Serializable> void copyObservers(IWithObserversEntity<T, PersonVO> source,
                                                       IWithObserversEntity<T, Person> target,
                                                       boolean copyIfNull) {
        // Observers
        if (copyIfNull || source.getObservers() != null) {
            if (CollectionUtils.isEmpty(source.getObservers())) {
                if (target.getId() != null && CollectionUtils.isNotEmpty(target.getObservers())) {
                    target.getObservers().clear();
                }
            } else {
                Set<Person> observers = target.getId() != null ? target.getObservers() : Sets.newHashSet();
                Map<Integer, Person> observersToRemove = Beans.splitById(observers);
                source.getObservers().stream()
                        .filter(Objects::nonNull)
                        .map(IEntity::getId)
                        .filter(Objects::nonNull)
                        .forEach(personId -> {
                            if (observersToRemove.remove(personId) == null) {
                                // Add new item
                                observers.add(load(Person.class, personId));
                            }
                        });

                // Remove deleted tableNames
                if (MapUtils.isNotEmpty(observersToRemove)) {
                    observers.removeAll(observersToRemove.values());
                }
                target.setObservers(observers);
            }
        }
    }

    public void copyVessel(IWithVesselSnapshotEntity<Integer, VesselSnapshotVO> source,
                           IWithVesselEntity<Integer, Vessel> target,
                           boolean copyIfNull) {
        // Vessel
        if (copyIfNull || (source.getVesselSnapshot() != null && source.getVesselSnapshot().getId() != null)) {
            if (source.getVesselSnapshot() == null || source.getVesselSnapshot().getId() == null) {
                target.setVessel(null);
            } else {
                target.setVessel(load(Vessel.class, source.getVesselSnapshot().getId()));
            }
        }
    }

    public <T extends Serializable> void copyQualityFlag(IDataVO<T> source,
                                                         IDataEntity<T> target,
                                                         boolean copyIfNull) {
        // Quality flag
        if (copyIfNull || source.getQualityFlagId() != null) {
            if (source.getQualityFlagId() == null) {
                target.setQualityFlag(load(QualityFlag.class, SumarisConfiguration.getInstance().getDefaultQualityFlagId()));
            } else {
                target.setQualityFlag(load(QualityFlag.class, source.getQualityFlagId()));
            }
        }
    }

    public <T extends Serializable> void copyProgram(IRootDataVO<T> source,
                                                     IRootDataEntity<T> target,
                                                     boolean copyIfNull) {
        // Program
        if (copyIfNull || (source.getProgram() != null && (source.getProgram().getId() != null || source.getProgram().getLabel() != null))) {
            if (source.getProgram() == null || (source.getProgram().getId() == null && source.getProgram().getLabel() == null)) {
                target.setProgram(null);
            }
            // Load by id
            else if (source.getProgram().getId() != null) {
                target.setProgram(load(Program.class, source.getProgram().getId()));
            }
            // Load by label
            else {
                throw new SumarisTechnicalException("Missing program.id !");
            }
        }
    }



}
