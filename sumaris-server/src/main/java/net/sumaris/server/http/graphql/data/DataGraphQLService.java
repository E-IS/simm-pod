package net.sumaris.server.http.graphql.data;

/*-
 * #%L
 * SUMARiS:: Server
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
import com.google.common.collect.Maps;
import io.leangen.graphql.annotations.*;
import net.sumaris.core.dao.referential.metier.MetierRepository;
import net.sumaris.core.dao.technical.SortDirection;
import net.sumaris.core.dao.technical.model.IEntity;
import net.sumaris.core.model.data.*;
import net.sumaris.core.service.data.*;
import net.sumaris.core.service.referential.PmfmService;
import net.sumaris.core.vo.administration.user.DepartmentVO;
import net.sumaris.core.vo.administration.user.PersonVO;
import net.sumaris.core.vo.data.*;
import net.sumaris.core.vo.filter.*;
import net.sumaris.core.vo.referential.MetierVO;
import net.sumaris.core.vo.referential.PmfmVO;
import net.sumaris.server.http.security.IsSupervisor;
import net.sumaris.server.http.security.IsUser;
import net.sumaris.server.service.administration.ImageService;
import net.sumaris.server.service.technical.ChangesPublisherService;
import org.apache.commons.collections4.CollectionUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class DataGraphQLService {

    @Autowired
    private VesselService vesselService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ObservedLocationService observedLocationService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private LandingService landingService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private VesselPositionService vesselPositionService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private PmfmService pmfmService;

    @Autowired
    protected PhysicalGearService physicalGearService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ChangesPublisherService changesPublisherService;

    @Autowired
    private MetierRepository metierRepository;

    /* -- Vessel -- */

    @GraphQLQuery(name = "vesselSnapshots", description = "Search in vessel snapshots")
    @Transactional(readOnly = true)
    @IsUser
    public List<VesselSnapshotVO> findVesselSnapshotsByFilter(@GraphQLArgument(name = "filter") VesselFilterVO filter,
                                                              @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                                              @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                                              @GraphQLArgument(name = "sortBy", defaultValue = VesselSnapshotVO.Fields.EXTERIOR_MARKING) String sort,
                                                              @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction
    ) {
        return vesselService.findSnapshotByFilter(filter, offset, size, sort,
                direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null);
    }

    @GraphQLQuery(name = "vessels", description = "Search in vessels")
    @Transactional(readOnly = true)
    @IsUser
    public List<VesselVO> findVesselByFilter(@GraphQLArgument(name = "filter") VesselFilterVO filter,
                                                              @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                                              @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                                              @GraphQLArgument(name = "sortBy") String sort,
                                                              @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction
    ) {
        return vesselService.findVesselsByFilter(filter, offset, size, sort,
                direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null);
    }

    @GraphQLQuery(name = "vesselsCount", description = "Get total vessels count")
    @Transactional(readOnly = true)
    @IsUser
    public long getVesselsCount(@GraphQLArgument(name = "filter") VesselFilterVO filter) {
        return vesselService.countVesselsByFilter(filter);
    }

    @GraphQLQuery(name = "vessel", description = "Get a vessel")
    @Transactional(readOnly = true)
    @IsUser
    public VesselVO getVesselById(@GraphQLArgument(name = "vesselId") int vesselId) {
        return vesselService.getVesselById(vesselId);
    }

    @GraphQLQuery(name = "vesselFeaturesHistory", description = "Get vessel features history")
    @Transactional(readOnly = true)
    @IsUser
    public List<VesselFeaturesVO> getVesselFeaturesHistory(@GraphQLArgument(name = "vesselId") Integer vesselId,
                                                           @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                                           @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                                           @GraphQLArgument(name = "sortBy", defaultValue = VesselFeaturesVO.Fields.START_DATE) String sort,
                                                           @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction) {
        return vesselService.getFeaturesByVesselId(vesselId, offset, size, sort, direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null);
    }

    @GraphQLQuery(name = "vesselRegistrationHistory", description = "Get vessel registration history")
    @Transactional(readOnly = true)
    @IsUser
    public List<VesselRegistrationVO> getVesselRegistrationHistory(@GraphQLArgument(name = "vesselId") Integer vesselId,
                                                     @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                                     @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                                     @GraphQLArgument(name = "sortBy", defaultValue = VesselRegistrationVO.Fields.START_DATE) String sort,
                                                     @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction) {
        return vesselService.getRegistrationsByVesselId(vesselId, offset, size, sort, direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null);
    }

    @GraphQLMutation(name = "saveVessel", description = "Create or update a vessel")
    @IsUser
    public VesselVO saveVessel(@GraphQLArgument(name = "vessel") VesselVO vessel) {
        return vesselService.save(vessel);
    }

    @GraphQLMutation(name = "saveVessels", description = "Create or update many vessels")
    @IsUser
    public List<VesselVO> saveVessels(@GraphQLArgument(name = "vessels") List<VesselVO> vessels) {
        return vesselService.save(vessels);
    }

    @GraphQLMutation(name = "deleteVessel", description = "Delete a vessel (by vessel features id)")
    @IsUser
    public void deleteVessel(@GraphQLArgument(name = "id") int id) {
        vesselService.delete(id);
    }

    @GraphQLMutation(name = "deleteVessels", description = "Delete many vessels (by vessel features ids)")
    @IsUser
    public void deleteVessels(@GraphQLArgument(name = "ids") List<Integer> ids) {
        vesselService.delete(ids);
    }


    /* -- Trip -- */

    @GraphQLQuery(name = "trips", description = "Search in trips")
    @Transactional(readOnly = true)
    @IsUser
    public List<TripVO> findTripsByFilter(@GraphQLArgument(name = "filter") TripFilterVO filter,
                                          @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                          @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                          @GraphQLArgument(name = "sortBy", defaultValue = TripVO.Fields.DEPARTURE_DATE_TIME) String sort,
                                          @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction,
                                          @GraphQLEnvironment() Set<String> fields
                                  ) {

        final List<TripVO> result = tripService.findByFilter(filter, offset, size, sort,
                direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null,
                getFetchOptions(fields));

        // Add additional properties if needed
        fillTrips(result, fields);

        return result;
    }

    @GraphQLQuery(name = "tripsCount", description = "Get total trips count")
    @Transactional(readOnly = true)
    @IsUser
    public long getTripsCount(@GraphQLArgument(name = "filter") TripFilterVO filter) {
        return tripService.countByFilter(filter);
    }

    @GraphQLQuery(name = "trip", description = "Get a trip, by id")
    @Transactional(readOnly = true)
    @IsUser
    public TripVO getTripById(@GraphQLArgument(name = "id") int id,
                              @GraphQLEnvironment() Set<String> fields) {
        final TripVO result = tripService.get(id);

        // Add additional properties if needed
        fillTripFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "saveTrip", description = "Create or update a trip")
    @IsUser
    public TripVO saveTrip(@GraphQLArgument(name = "trip") TripVO trip,
                           @GraphQLArgument(name = "withOperation", defaultValue = "false") boolean withOperation,
                           @GraphQLEnvironment() Set<String> fields) {
        final TripVO result = tripService.save(trip, withOperation);

        // Add additional properties if needed
        fillTripFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "saveTrips", description = "Create or update many trips")
    @IsUser
    public List<TripVO> saveTrips(@GraphQLArgument(name = "trips") List<TripVO> trips,
                                  @GraphQLArgument(name = "withOperation", defaultValue = "false") boolean withOperation,
                                  @GraphQLEnvironment() Set<String> fields) {
        final List<TripVO> result = tripService.save(trips, withOperation);

        // Add additional properties if needed
        fillTrips(result, fields);

        return result;
    }

    @GraphQLMutation(name = "deleteTrip", description = "Delete a trip")
    @IsUser
    public void deleteTrip(@GraphQLArgument(name = "id") int id) {
        tripService.delete(id);
    }

    @GraphQLMutation(name = "deleteTrips", description = "Delete many trips")
    @IsUser
    public void deleteTrips(@GraphQLArgument(name = "ids") List<Integer> ids) {
        tripService.delete(ids);
    }

    @GraphQLSubscription(name = "updateTrip", description = "Subscribe to changes on a trip")
    @IsUser
    public Publisher<TripVO> updateTrip(@GraphQLArgument(name = "id") final int id,
                                        @GraphQLArgument(name = "interval", defaultValue = "30", description = "Minimum interval to get changes, in seconds.") final Integer minIntervalInSecond) {

        Preconditions.checkArgument(id >= 0, "Invalid id");
        return changesPublisherService.getPublisher(Trip.class, TripVO.class, id, minIntervalInSecond, true);
    }

    @GraphQLMutation(name = "controlTrip", description = "Control a trip")
    @IsUser
    public TripVO controlTrip(@GraphQLArgument(name = "trip") TripVO trip, @GraphQLEnvironment() Set<String> fields) {
        final TripVO result = tripService.control(trip);

        // Add additional properties if needed
        fillTripFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "validateTrip", description = "Validate a trip")
    @IsSupervisor
    public TripVO validateTrip(@GraphQLArgument(name = "trip") TripVO trip, @GraphQLEnvironment() Set<String> fields) {
        final TripVO result = tripService.validate(trip);

        // Add additional properties if needed
        fillTripFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "unvalidateTrip", description = "Unvalidate a trip")
    @IsSupervisor
    public TripVO unvalidateTrip(@GraphQLArgument(name = "trip") TripVO trip, @GraphQLEnvironment() Set<String> fields) {
        final TripVO result = tripService.unvalidate(trip);

        // Add additional properties if needed
        fillTripFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "qualifyTrip", description = "Qualify a trip")
    @IsSupervisor
    public TripVO qualifyTrip(@GraphQLArgument(name = "trip") TripVO trip, @GraphQLEnvironment() Set<String> fields) {
        final TripVO result = tripService.qualify(trip);

        // Add additional properties if needed
        fillTripFields(result, fields);

        return result;
    }

    /* -- Gears -- */

    @GraphQLQuery(name = "gears", description = "Get operation's gears")
    public List<PhysicalGearVO> getGearsByTrip(@GraphQLContext TripVO trip) {
        return physicalGearService.getPhysicalGearByTripId(trip.getId());
    }

    /* -- Observed location -- */

    @GraphQLQuery(name = "observedLocations", description = "Search in observed locations")
    @Transactional(readOnly = true)
    @IsUser
    public List<ObservedLocationVO> findObservedLocationsByFilter(@GraphQLArgument(name = "filter") ObservedLocationFilterVO filter,
                                                                @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                                                @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                                                @GraphQLArgument(name = "sortBy", defaultValue = ObservedLocationVO.Fields.START_DATE_TIME) String sort,
                                                                @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction,
                                                                @GraphQLEnvironment() Set<String> fields
    ) {
        final List<ObservedLocationVO> result = observedLocationService.findByFilter(filter, offset, size, sort,
                direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null,
                getFetchOptions(fields));

        // Add additional properties if needed
        fillObservedLocationsFields(result, fields);

        return result;
    }

    @GraphQLQuery(name = "observedLocationsCount", description = "Get total number of observed locations")
    @Transactional(readOnly = true)
    @IsUser
    public long getObservedLocationsCount(@GraphQLArgument(name = "filter") ObservedLocationFilterVO filter) {
        return observedLocationService.countByFilter(filter);
    }

    @GraphQLQuery(name = "observedLocation", description = "Get an observed location, by id")
    @Transactional(readOnly = true)
    @IsUser
    public ObservedLocationVO getObservedLocationById(@GraphQLArgument(name = "id") int id,
                              @GraphQLEnvironment() Set<String> fields) {
        final ObservedLocationVO result = observedLocationService.get(id);

        // Add additional properties if needed
        fillObservedLocationFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "saveObservedLocation", description = "Create or update an observed location")
    @IsUser
    public ObservedLocationVO saveObservedLocation(@GraphQLArgument(name = "observedLocation") ObservedLocationVO observedLocation, @GraphQLEnvironment() Set<String> fields) {
        final ObservedLocationVO result = observedLocationService.save(observedLocation, false);

        // Fill expected fields
        fillObservedLocationFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "saveObservedLocations", description = "Create or update many observed locations")
    @IsUser
    public List<ObservedLocationVO> saveObservedLocations(@GraphQLArgument(name = "observedLocations") List<ObservedLocationVO> observedLocations, @GraphQLEnvironment() Set<String> fields) {
        final List<ObservedLocationVO> result = observedLocationService.save(observedLocations, false);

        // Fill expected fields
        fillObservedLocationsFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "deleteObservedLocation", description = "Delete an observed location")
    @IsUser
    public void deleteObservedLocation(@GraphQLArgument(name = "id") int id) {
        observedLocationService.delete(id);
    }

    @GraphQLMutation(name = "deleteObservedLocations", description = "Delete many observed locations")
    @IsUser
    public void deleteObservedLocations(@GraphQLArgument(name = "ids") List<Integer> ids) {
        observedLocationService.delete(ids);
    }

    @GraphQLSubscription(name = "updateObservedLocation", description = "Subscribe to changes on an observed location")
    @IsUser
    public Publisher<ObservedLocationVO> updateObservedLocation(@GraphQLArgument(name = "id") final int id,
                                        @GraphQLArgument(name = "interval", defaultValue = "30", description = "Minimum interval to get changes, in seconds.") final Integer minIntervalInSecond) {

        Preconditions.checkArgument(id >= 0, "Invalid id");
        return changesPublisherService.getPublisher(ObservedLocation.class, ObservedLocationVO.class, id, minIntervalInSecond, true);
    }

    @GraphQLMutation(name = "controlObservedLocation", description = "Control an observed location")
    @IsUser
    public ObservedLocationVO controlObservedLocation(@GraphQLArgument(name = "observedLocation") ObservedLocationVO observedLocation, @GraphQLEnvironment() Set<String> fields) {
        final ObservedLocationVO result = observedLocationService.control(observedLocation);

        // Add additional properties if needed
        fillObservedLocationFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "validateObservedLocation", description = "Validate an observed location")
    @IsSupervisor
    public ObservedLocationVO validateObservedLocation(@GraphQLArgument(name = "observedLocation") ObservedLocationVO observedLocation, @GraphQLEnvironment() Set<String> fields) {
        final ObservedLocationVO result = observedLocationService.validate(observedLocation);

        // Add additional properties if needed
        fillObservedLocationFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "unvalidateObservedLocation", description = "Unvalidate an observed location")
    @IsSupervisor
    public ObservedLocationVO unvalidateObservedLocation(@GraphQLArgument(name = "observedLocation") ObservedLocationVO observedLocation, @GraphQLEnvironment() Set<String> fields) {
        final ObservedLocationVO result = observedLocationService.unvalidate(observedLocation);

        // Add additional properties if needed
        fillObservedLocationFields(result, fields);

        return result;
    }


    /* -- Sales -- */

    @GraphQLQuery(name = "sales", description = "Get trip's sales")
    public List<SaleVO> getSalesByTrip(@GraphQLContext TripVO trip) {
        return saleService.getAllByTripId(trip.getId());
    }

    @GraphQLQuery(name = "sale", description = "Get trip's unique sale")
    public SaleVO getUniqueSaleByTrip(@GraphQLContext TripVO trip) {
        List<SaleVO> sales = saleService.getAllByTripId(trip.getId());
        return CollectionUtils.isEmpty(sales) ? null : CollectionUtils.extractSingleton(sales);
    }

    /* -- Operations -- */

    @GraphQLQuery(name = "operations", description = "Get trip's operations")
    @Transactional(readOnly = true)
    @IsUser
    public List<OperationVO> getOperationsByTripId(@GraphQLArgument(name = "filter") OperationFilterVO filter,
                                                   @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                                   @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                                   @GraphQLArgument(name = "sortBy", defaultValue = OperationVO.Fields.START_DATE_TIME) String sort,
                                                   @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction) {
        Preconditions.checkNotNull(filter, "Missing tripFilter or tripFilter.tripId");
        Preconditions.checkNotNull(filter.getTripId(), "Missing tripFilter or tripFilter.tripId");
        return operationService.getAllByTripId(filter.getTripId(), offset, size, sort, direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null);
    }

    @GraphQLQuery(name = "operations", description = "Get trip's operations")
    public List<OperationVO> getOperationsByTrip(@GraphQLContext TripVO trip) {
        if (CollectionUtils.isNotEmpty(trip.getOperations())) {
            return trip.getOperations();
        }
        return operationService.getAllByTripId(trip.getId(), 0, 1000, OperationVO.Fields.START_DATE_TIME, SortDirection.ASC);
    }

    @GraphQLQuery(name = "operation", description = "Get an operation")
    @Transactional(readOnly = true)
    @IsUser
    public OperationVO getOperation(@GraphQLArgument(name = "id") int id) {
        return operationService.get(id);
    }

    @GraphQLMutation(name = "saveOperations", description = "Save operations")
    @IsUser
    public List<OperationVO> saveOperations(@GraphQLArgument(name = "operations") List<OperationVO> operations) {
        return operationService.save(operations);
    }

    @GraphQLMutation(name = "saveOperation", description = "Create or update an operation")
    @IsUser
    public OperationVO saveOperation(@GraphQLArgument(name = "operation") OperationVO operation) {
        return operationService.save(operation);
    }

    @GraphQLMutation(name = "deleteOperation", description = "Delete an operation")
    @IsUser
    public void deleteOperation(@GraphQLArgument(name = "id") int id) {
        operationService.delete(id);
    }

    @GraphQLMutation(name = "deleteOperations", description = "Delete many operations")
    @IsUser
    public void deleteOperations(@GraphQLArgument(name = "ids") List<Integer> ids) {
        operationService.delete(ids);
    }

    @GraphQLSubscription(name = "updateOperation", description = "Subscribe to changes on an operation")
    @IsUser
    public Publisher<OperationVO> updateOperation(@GraphQLArgument(name = "id") final int id,
                                        @GraphQLArgument(name = "interval", defaultValue = "30", description = "Minimum interval to get changes, in seconds.") final Integer minIntervalInSecond) {

        Preconditions.checkArgument(id >= 0, "Invalid id");
        return changesPublisherService.getPublisher(Operation.class, OperationVO.class, id, minIntervalInSecond, true);
    }

    /* -- Vessel position -- */

    @GraphQLQuery(name = "positions", description = "Get operation's position")
    public List<VesselPositionVO> getPositionsByOperation(@GraphQLContext OperationVO operation) {
        // Avoid a reloading (e.g. when saving)
        if (CollectionUtils.isNotEmpty(operation.getPositions())) {
            return operation.getPositions();
        }
        return vesselPositionService.getAllByOperationId(operation.getId(), 0, 100, VesselPositionVO.Fields.DATE_TIME, SortDirection.ASC);
    }

    /* -- Vessel features -- */

    @GraphQLQuery(name = "vesselSnapshot", description = "Get trip vessel snapshot")
    public VesselSnapshotVO getVesselFeaturesByTrip(@GraphQLContext TripVO trip) {
        return vesselService.getSnapshotByIdAndDate(trip.getVesselSnapshot().getId(), trip.getDepartureDateTime());
    }

    /* -- Sample -- */

    @GraphQLQuery(name = "samples", description = "Get operation's samples")
    public List<SampleVO> getSamplesByOperation(@GraphQLContext OperationVO operation) {
        // Avoid a reloading (e.g. when saving)
        if (CollectionUtils.isNotEmpty(operation.getSamples())) {
            return operation.getSamples();
        }

        return sampleService.getAllByOperationId(operation.getId());
    }


    @GraphQLQuery(name = "samples", description = "Get landing's samples")
    public List<SampleVO> getSamplesByLanding(@GraphQLContext LandingVO landing) {
        // Avoid a reloading (e.g. when saving)
        if (CollectionUtils.isNotEmpty(landing.getSamples())) {
            return landing.getSamples();
        }

        return sampleService.getAllByLandingId(landing.getId());
    }

    /* -- Batch -- */

    @GraphQLQuery(name = "batches", description = "Get operation's batches")
    public List<BatchVO> getBatchesByOperation(@GraphQLContext OperationVO operation) {
        // Avoid a reloading (e.g. when saving): reuse existing VO
        if (operation.getBatches() != null) {
            return operation.getBatches();
        }

        // Reload, if not exist in VO
        return batchService.getAllByOperationId(operation.getId());
    }

    /* -- Landings -- */

    @GraphQLQuery(name = "landings", description = "Search in landings")
    @Transactional(readOnly = true)
    //@IsUser
    public List<LandingVO> findAllLandings(@GraphQLArgument(name = "filter") LandingFilterVO filter,
                                           @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
                                           @GraphQLArgument(name = "size", defaultValue = "1000") Integer size,
                                           @GraphQLArgument(name = "sortBy", defaultValue = LandingVO.Fields.DATE_TIME) String sort,
                                           @GraphQLArgument(name = "sortDirection", defaultValue = "asc") String direction,
                                           @GraphQLEnvironment() Set<String> fields
    ) {
        final List<LandingVO> result = landingService.findAll(filter, offset, size, sort,
                direction != null ? SortDirection.valueOf(direction.toUpperCase()) : null,
                getFetchOptions(fields));

        // Add additional properties if needed
        fillLandingsFields(result, fields);

        return result;
    }

    @GraphQLQuery(name = "landingsCount", description = "Get total number of landings")
    @Transactional(readOnly = true)
    @IsUser
    public long countLandings(@GraphQLArgument(name = "filter") LandingFilterVO filter) {
        return landingService.countByFilter(filter);
    }

    @GraphQLQuery(name = "landing", description = "Get an observed location, by id")
    @Transactional(readOnly = true)
    @IsUser
    public LandingVO getLanding(@GraphQLArgument(name = "id") int id,
                                @GraphQLEnvironment() Set<String> fields) {
        final LandingVO result = landingService.get(id);

        // Add additional properties if needed
        fillLandingFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "saveLanding", description = "Create or update an landing")
    @IsUser
    public LandingVO saveLanding(@GraphQLArgument(name = "landing") LandingVO landing,
                                 @GraphQLEnvironment() Set<String> fields) {
        final LandingVO result = landingService.save(landing);

        // Fill expected fields
        fillLandingFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "saveLandings", description = "Create or update many landings")
    @IsUser
    public List<LandingVO> saveLandings(@GraphQLArgument(name = "landings") List<LandingVO> landings, @GraphQLEnvironment() Set<String> fields) {
        final List<LandingVO> result = landingService.save(landings);

        // Fill expected fields
        fillLandingsFields(result, fields);

        return result;
    }

    @GraphQLMutation(name = "deleteLanding", description = "Delete an observed location")
    @IsUser
    public void deleteLanding(@GraphQLArgument(name = "id") int id) {
        landingService.delete(id);
    }

    @GraphQLMutation(name = "deleteLandings", description = "Delete many observed locations")
    @IsUser
    public void deleteLandings(@GraphQLArgument(name = "ids") List<Integer> ids) {
        landingService.delete(ids);
    }

    @GraphQLSubscription(name = "updateLanding", description = "Subscribe to changes on an landing")
    @IsUser
    public Publisher<LandingVO> updateLanding(@GraphQLArgument(name = "id") final int id,
                                                                @GraphQLArgument(name = "interval", defaultValue = "30", description = "Minimum interval to get changes, in seconds.") final Integer minIntervalInSecond) {

        Preconditions.checkArgument(id >= 0, "Invalid id");
        return changesPublisherService.getPublisher(Landing.class, LandingVO.class, id, minIntervalInSecond, true);
    }

    /* -- Measurements -- */

    // Trip
    @GraphQLQuery(name = "measurements", description = "Get trip's measurements")
    public List<MeasurementVO> getTripVesselUseMeasurements(@GraphQLContext TripVO trip) {
        return measurementService.getTripVesselUseMeasurements(trip.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get trip's measurements")
    public Map<Integer, String> getTripVesselUseMeasurementsMap(@GraphQLContext TripVO trip) {
        if (trip.getMeasurementValues() != null) {
            return trip.getMeasurementValues();
        }
        return measurementService.getTripVesselUseMeasurementsMap(trip.getId());
    }

    // Operation
    @GraphQLQuery(name = "measurements", description = "Get operation's measurements")
    public List<MeasurementVO> getOperationVesselUseMeasurements(@GraphQLContext OperationVO operation) {
        return measurementService.getOperationVesselUseMeasurements(operation.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get operation's measurements")
    public Map<Integer, String> getOperationVesselUseMeasurementsMap(@GraphQLContext OperationVO operation) {
        if (operation.getMeasurementValues() != null) {
            return operation.getMeasurementValues();
        }
        return measurementService.getOperationVesselUseMeasurementsMap(operation.getId());
    }

    @GraphQLQuery(name = "gearMeasurements", description = "Get operation's gear measurements")
    public List<MeasurementVO> getOperationGearUseMeasurements(@GraphQLContext OperationVO operation) {
        return measurementService.getOperationGearUseMeasurements(operation.getId());
    }

    @GraphQLQuery(name = "gearMeasurementValues", description = "Get operation's gear measurements")
    public Map<Integer, String> getOperationGearUseMeasurementsMap(@GraphQLContext OperationVO operation) {
        if (operation.getGearMeasurementValues() != null) {
            return operation.getGearMeasurementValues();
        }
        return measurementService.getOperationGearUseMeasurementsMap(operation.getId());
    }


    // Physical gear
    @GraphQLQuery(name = "measurements", description = "Get physical gear measurements")
    public List<MeasurementVO> getPhysicalGearMeasurements(@GraphQLContext PhysicalGearVO physicalGear) {
        return measurementService.getPhysicalGearMeasurements(physicalGear.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get physical gear measurements")
    public Map<Integer, String> getPhysicalGearMeasurementsMap(@GraphQLContext PhysicalGearVO physicalGear) {
        if (physicalGear.getMeasurementValues() != null) {
            return physicalGear.getMeasurementValues();
        }
        return measurementService.getPhysicalGearMeasurementsMap(physicalGear.getId());
    }

    // Metier
    @GraphQLQuery(name = "metier", description = "Get operation's metier")
    public MetierVO getOperationMetier(@GraphQLContext OperationVO operation) {
        // Already fetch: use it
        if (operation.getMetier() == null || operation.getMetier().getTaxonGroup() != null) {
            return operation.getMetier();
        }
        Integer metierId = operation.getMetier().getId();
        if (metierId == null) return null;

        return metierRepository.getById(metierId);
    }

    // Sample
    @GraphQLQuery(name = "measurements", description = "Get sample measurements")
    public List<MeasurementVO> getSampleMeasurements(@GraphQLContext SampleVO sample) {
        if (sample.getMeasurements() != null) {
            return sample.getMeasurements();
        }
        return measurementService.getSampleMeasurements(sample.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get measurement values (as a key/value map, using pmfmId as key)")
    public Map<Integer, String> getSampleMeasurementValues(@GraphQLContext SampleVO sample) {
        if (sample.getMeasurementValues() != null) {
            return sample.getMeasurementValues();
        }
        return measurementService.getSampleMeasurementsMap(sample.getId());
    }

    // Batch
    @GraphQLQuery(name = "measurementValues", description = "Get measurement values (as a key/value map, using pmfmId as key)")
    public Map<Integer, String> getBatchMeasurementValues(@GraphQLContext BatchVO batch) {
        if (batch.getMeasurementValues() != null) {
            return batch.getMeasurementValues();
        }
        Map<Integer, String> map = Maps.newHashMap();
        map.putAll(measurementService.getBatchSortingMeasurementsMap(batch.getId()));
        map.putAll(measurementService.getBatchQuantificationMeasurementsMap(batch.getId()));
        return map;
    }

    // Observed location
    @GraphQLQuery(name = "measurements", description = "Get measurement values")
    public List<MeasurementVO> getObservedLocationMeasurements(@GraphQLContext ObservedLocationVO observedLocation) {
        if (observedLocation.getMeasurements() != null) {
            return observedLocation.getMeasurements();
        }
        return measurementService.getObservedLocationMeasurements(observedLocation.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get measurement values (as a key/value map, using pmfmId as key)")
    public Map<Integer, String> getObservedLocationMeasurementsMap(@GraphQLContext ObservedLocationVO observedLocation) {
        if (observedLocation.getMeasurementValues() != null) {
            return observedLocation.getMeasurementValues();
        }
        return measurementService.getObservedLocationMeasurementsMap(observedLocation.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get measurement values (as a key/value map, using pmfmId as key)")
    public Map<Integer, String> getLandingMeasurementsMap(@GraphQLContext LandingVO landing) {
        if (landing.getMeasurementValues() != null) {
            return landing.getMeasurementValues();
        }
        return measurementService.getLandingMeasurementsMap(landing.getId());
    }

    // Measurement pmfm
    @GraphQLQuery(name = "pmfm", description = "Get measurement's pmfm")
    public PmfmVO getMeasurementPmfm(@GraphQLContext MeasurementVO measurement) {
        return pmfmService.get(measurement.getPmfmId());
    }

    // Vessel
    @GraphQLQuery(name = "measurements", description = "Get vessel's physical measurements")
    public List<MeasurementVO> getVesselFeaturesMeasurements(@GraphQLContext VesselSnapshotVO vesselSnapshot) {
        return measurementService.getVesselFeaturesMeasurements(vesselSnapshot.getId());
    }

    @GraphQLQuery(name = "measurementValues", description = "Get vessel's physical measurements")
    public Map<Integer, String> getVesselFeaturesMeasurementsMap(@GraphQLContext VesselSnapshotVO vesselSnapshot) {
        if (vesselSnapshot.getMeasurementValues() == null) {
            return measurementService.getVesselFeaturesMeasurementsMap(vesselSnapshot.getId());
        }
        return vesselSnapshot.getMeasurementValues();
    }


    /* -- protected methods -- */

    protected void fillTripFields(TripVO trip, Set<String> fields) {
        // Add image if need
        fillImages(trip, fields);

        // Add vessel if need
        fillVesselSnapshot(trip, fields);
    }

    protected void fillTrips(List<TripVO> trips, Set<String> fields) {
        // Add image if need
        fillImages(trips, fields);

        // Add vessel if need
        fillVesselSnapshot(trips, fields);
    }

    protected void fillObservedLocationFields(ObservedLocationVO observedLocation, Set<String> fields) {
        // Add image if need
        fillImages(observedLocation, fields);
    }

    protected void fillObservedLocationsFields(List<ObservedLocationVO> observedLocations, Set<String> fields) {
        // Add image if need
        fillImages(observedLocations, fields);
    }

    protected void fillLandingFields(LandingVO landing, Set<String> fields) {
        // Add image if need
        fillImages(landing, fields);

        // Add vessel if need
        fillVesselSnapshot(landing, fields);
    }

    protected void fillLandingsFields(List<LandingVO> landings, Set<String> fields) {
        // Add image if need
        fillImages(landings, fields);

        // Add vessel if need
        fillVesselSnapshot(landings, fields);
    }

    protected boolean hasImageField(Set<String> fields) {
        return fields.contains(TripVO.Fields.RECORDER_DEPARTMENT + File.separator + DepartmentVO.Fields.LOGO) ||
                fields.contains(TripVO.Fields.RECORDER_PERSON + File.separator + PersonVO.Fields.AVATAR);
    }

    protected boolean hasVesselFeaturesField(Set<String> fields) {
        return fields.contains(TripVO.Fields.VESSEL_SNAPSHOT + File.separator + VesselSnapshotVO.Fields.EXTERIOR_MARKING)
                || fields.contains(TripVO.Fields.VESSEL_SNAPSHOT + File.separator + VesselSnapshotVO.Fields.NAME);
    }

    protected <T extends IRootDataVO<?>> List<T> fillImages(final List<T> results) {
        results.forEach(this::fillImages);
        return results;
    }

    protected <T extends IRootDataVO<?>> T fillImages(T result) {
        if (result != null) {

            // Fill avatar on recorder department (if not null)
            imageService.fillLogo(result.getRecorderDepartment());

            // Fill avatar on recorder persons (if not null)
            imageService.fillAvatar(result.getRecorderPerson());
        }

        return result;
    }

    protected <T extends IRootDataVO<?>> List<T> fillImages(final List<T> results, Set<String> fields) {
        if (hasImageField(fields)) results.forEach(this::fillImages);
        return results;
    }

    protected <T extends IRootDataVO<?>> T fillImages(T result, Set<String> fields) {
        if (hasImageField(fields) && result != null) {

            // Fill avatar on recorder department (if not null)
            imageService.fillLogo(result.getRecorderDepartment());

            // Fill avatar on recorder persons (if not null)
            imageService.fillAvatar(result.getRecorderPerson());
        }

        return result;
    }

    protected <T extends IWithVesselSnapshotEntity<?, VesselSnapshotVO>> void fillVesselSnapshot(T bean, Set<String> fields) {
        // Add vessel if need
        if (hasVesselFeaturesField(fields)) {
            if (bean.getVesselSnapshot() != null && bean.getVesselSnapshot().getId() != null) {
                bean.setVesselSnapshot(vesselService.getSnapshotByIdAndDate(bean.getVesselSnapshot().getId(), bean.getVesselDateTime()));
            }
        }
    }

    protected <T extends IWithVesselSnapshotEntity<?, VesselSnapshotVO>> void fillVesselSnapshot(List<T> beans, Set<String> fields) {
        // Add vessel if need
        if (hasVesselFeaturesField(fields)) {
            beans.forEach(bean -> {
                if (bean.getVesselSnapshot() != null && bean.getVesselSnapshot().getId() != null) {
                    bean.setVesselSnapshot(vesselService.getSnapshotByIdAndDate(bean.getVesselSnapshot().getId(), bean.getVesselDateTime()));
                }
            });
        }
    }

    protected DataFetchOptions getFetchOptions(Set<String> fields) {
        return DataFetchOptions.builder()
                .withObservers(fields.contains(IWithObserversEntity.Fields.OBSERVERS + "/" + IEntity.Fields.ID))
                .withRecorderDepartment(fields.contains(IWithRecorderDepartmentEntity.Fields.RECORDER_DEPARTMENT + "/" + IEntity.Fields.ID))
                .withRecorderPerson(fields.contains(IWithRecorderPersonEntity.Fields.RECORDER_PERSON + "/" + IEntity.Fields.ID))
                .build();
    }
}
