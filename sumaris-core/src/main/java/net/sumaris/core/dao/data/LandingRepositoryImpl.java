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

import net.sumaris.core.dao.referential.location.LocationDao;
import net.sumaris.core.dao.technical.model.IEntity;
import net.sumaris.core.model.data.Landing;
import net.sumaris.core.model.data.ObservedLocation;
import net.sumaris.core.model.data.Trip;
import net.sumaris.core.model.referential.location.Location;
import net.sumaris.core.util.Beans;
import net.sumaris.core.vo.administration.programStrategy.ProgramVO;
import net.sumaris.core.vo.data.DataFetchOptions;
import net.sumaris.core.vo.data.LandingVO;
import net.sumaris.core.vo.filter.LandingFilterVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LandingRepositoryImpl
        extends RootDataRepositoryImpl<Landing, Integer, LandingVO, LandingFilterVO>
        implements LandingRepositoryExtend {



    private static final Logger log =
            LoggerFactory.getLogger(LandingRepositoryImpl.class);

    @Autowired
    private LocationDao locationDao;

    public LandingRepositoryImpl(EntityManager entityManager) {
        super(Landing.class, entityManager);
    }


    @Override
    public LandingVO toVO(Landing source) {
        return super.toVO(source);
    }

    @Override
    public Specification<Landing> toSpecification(LandingFilterVO filter) {
        if (filter == null) return null;

        return Specification.where(and(
                hasObservedLocationId(filter.getObservedLocationId()),
                hasTripId(filter.getTripId()),
                betweenDate(filter.getStartDate(), filter.getEndDate()),
                hasLocationId(filter.getLocationId()))
        );
    }

    public Class<LandingVO> getVOClass() {
        return LandingVO.class;
    }

    //@Override
    public List<LandingVO> saveAllByObservedLocationId(int observedLocationId, List<LandingVO> sources) {
        // Load parent entity
        ObservedLocation parent = get(ObservedLocation.class, observedLocationId);
        ProgramVO parentProgram = new ProgramVO();
        parentProgram.setId(parent.getProgram().getId());

        // Remember existing entities
        final List<Integer> sourcesIdsToRemove = Beans.collectIds(Beans.getList(parent.getLandings()));

        // Save each gears
        List<LandingVO> result = sources.stream().map(source -> {
            source.setObservedLocationId(observedLocationId);
            source.setProgram(parentProgram);

            if (source.getId() != null) {
                sourcesIdsToRemove.remove(source.getId());
            }
            return save(source);
        }).collect(Collectors.toList());

        // Remove unused entities
        if (CollectionUtils.isNotEmpty(sourcesIdsToRemove)) {
            sourcesIdsToRemove.forEach(this::deleteById);
        }

        return result;
    }

    @Override
    public void toVO(Landing source, LandingVO target, DataFetchOptions fetchOptions, boolean copyIfNull) {
        super.toVO(source, target, fetchOptions, copyIfNull);

        // location
        target.setLocation(locationDao.toLocationVO(source.getLocation()));

        // Parent link
        if (source.getObservedLocation() != null) {
            target.setObservedLocationId(source.getObservedLocation().getId());
        }
        if (source.getTrip() != null) {
            target.setTripId(source.getTrip().getId());
        }
    }

    @Override
    public void toEntity(LandingVO source, Landing target, boolean copyIfNull) {

        DataDaos.copyRootDataProperties(getEntityManager(), source, target, copyIfNull);

        // Vessel
        copyVessel(source, target, copyIfNull);

        // Observers
        copyObservers(source, target, copyIfNull);

        // Landing location
        if (copyIfNull || source.getLocation() != null) {
            if (source.getLocation() == null || source.getLocation().getId() == null) {
                target.setLocation(null);
            } else {
                target.setLocation(load(Location.class, source.getLocation().getId()));
            }
        }

        // Observed Location
        Integer observedLocationId = source.getObservedLocationId() != null ? source.getObservedLocationId() : (source.getObservedLocation() != null ? source.getObservedLocation().getId() : null);
        if (copyIfNull || (observedLocationId != null)) {
            if (observedLocationId == null) {
                target.setObservedLocation(null);
            } else {
                target.setObservedLocation(load(ObservedLocation.class, observedLocationId));
            }
        }

        // Trip
        Integer tripId = source.getTripId() != null ? source.getTripId() : (source.getTrip() != null ? source.getTrip().getId() : null);
        if (copyIfNull || (tripId != null)) {
            if (tripId == null) {
                target.setTrip(null);
            } else {
                target.setTrip(load(Trip.class, tripId));
            }
        }


    }

}
