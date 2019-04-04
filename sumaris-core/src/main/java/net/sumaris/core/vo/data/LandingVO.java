package net.sumaris.core.vo.data;

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

import lombok.Data;
import net.sumaris.core.dao.technical.model.IUpdateDateEntityBean;
import net.sumaris.core.model.data.IWithRecorderDepartmentEntityBean;
import net.sumaris.core.model.data.IWithRecorderPersonEntityBean;
import net.sumaris.core.vo.administration.user.DepartmentVO;
import net.sumaris.core.vo.administration.user.PersonVO;
import net.sumaris.core.vo.referential.LocationVO;
import net.sumaris.core.vo.referential.ReferentialVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class LandingVO implements IUpdateDateEntityBean<Integer, Date>,
        IWithRecorderPersonEntityBean<Integer, PersonVO>,
        IWithRecorderDepartmentEntityBean<Integer, DepartmentVO>,
        IWithVesselFeaturesVO<Integer, VesselFeaturesVO>{

    public static final String PROPERTY_LANDING_DATE_TIME = "landingDateTime";
    public static final String PROPERTY_TRIP = "trip";

    private Integer id;
    private String comments;
    private Date creationDate;
    private Date updateDate;
    private Date controlDate;
    private Date validationDate;
    private Integer qualityFlagId;
    private DepartmentVO recorderDepartment;
    private PersonVO recorderPerson;

    private VesselFeaturesVO vesselFeatures;

    private Date landingDateTime;
    private LocationVO landingLocation;

    private Set<PersonVO> observers;
    private List<SampleVO> samples;

    private TripVO trip;
    private Integer tripId;

    private ObservedLocationVO observedLocation;
    private Integer observedLocationId;

    private List<MeasurementVO> measurements;
    private Map<Integer, String> measurementValues;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}