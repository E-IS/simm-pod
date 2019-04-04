package net.sumaris.core.model.data;

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
import net.sumaris.core.model.administration.user.Department;
import net.sumaris.core.model.referential.pmfm.Pmfm;
import net.sumaris.core.model.referential.pmfm.QualitativeValue;
import net.sumaris.core.model.referential.QualityFlag;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="physical_gear_measurement")
public class PhysicalGearMeasurement implements IMeasurementEntity {

    public static final String PROPERTY_RANK_ORDER = "rankOrder";
    public static final String PROPERTY_PHYSICAL_GEAR = "physicalGear";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PHYSICAL_GEAR_MEASUREMENT_SEQ")
    @SequenceGenerator(name = "PHYSICAL_GEAR_MEASUREMENT_SEQ", sequenceName="PHYSICAL_GEAR_MEASUREMENT_SEQ")
    private Integer id;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorder_department_fk", nullable = false)
    private Department recorderDepartment;

    @Column(length = LENGTH_COMMENTS)
    private String comments;

    @Column(name="control_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date controlDate;

    @Column(name="qualification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qualificationDate;

    @Column(name="qualification_comments", length = LENGTH_COMMENTS)
    private String qualificationComments;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = QualityFlag.class)
    @JoinColumn(name = "quality_flag_fk", nullable = false)
    private QualityFlag qualityFlag;

    @Column(name = "numerical_value")
    private Double numericalValue;

    @Column(name = "alphanumerical_value", length = 40)
    private String alphanumericalValue;

    @Column(name = "digit_count")
    private Integer digitCount;

    @Column(name = "precision_value")
    private Double precisionValue;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = QualitativeValue.class)
    @JoinColumn(name = "qualitative_value_fk")
    private QualitativeValue qualitativeValue;

    @Column(name = "rank_order", nullable = false)
    private Integer rankOrder;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Pmfm.class)
    @JoinColumn(name = "pmfm_fk", nullable = false)
    private Pmfm pmfm;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PhysicalGear.class)
    @JoinColumn(name = "physical_gear_fk", nullable = false)
    private PhysicalGear physicalGear;

}