package net.sumaris.core.model.file.ices;

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
import net.sumaris.core.dao.technical.model.IEntityBean;
import net.sumaris.core.dao.technical.schema.DatabaseTableEnum;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "file_ices_species_length")
public class FileIcesSpeciesLength implements Serializable, IEntityBean<Integer> {

    public static final DatabaseTableEnum TABLE = DatabaseTableEnum.FILE_ICES_SPECIES_LENGTH;

    public static final String COLUMN_SAMPLING_TYPE = FileIcesStation.COLUMN_SAMPLING_TYPE;
    public static final String COLUMN_VESSEL_FLAG_COUNTRY = FileIcesStation.COLUMN_VESSEL_FLAG_COUNTRY;
    public static final String COLUMN_LANDING_COUNTRY = FileIcesStation.COLUMN_LANDING_COUNTRY;
    public static final String COLUMN_YEAR = FileIcesStation.COLUMN_YEAR;
    public static final String COLUMN_PROJECT = FileIcesStation.COLUMN_PROJECT;
    public static final String COLUMN_TRIP_CODE = FileIcesStation.COLUMN_TRIP_CODE;
    public static final String COLUMN_STATION_NUMBER = FileIcesStation.COLUMN_STATION_NUMBER;

    public static final String COLUMN_SPECIES = FileIcesSpeciesList.COLUMN_SPECIES;
    public static final String COLUMN_CATCH_CATEGORY = FileIcesSpeciesList.COLUMN_CATCH_CATEGORY;
    public static final String COLUMN_LANDING_CATEGORY = FileIcesSpeciesList.COLUMN_LANDING_CATEGORY;
    public static final String COLUMN_COMMERCIAL_SIZE_CATEGORY_SCALE = FileIcesSpeciesList.COLUMN_COMMERCIAL_SIZE_CATEGORY_SCALE;
    public static final String COLUMN_COMMERCIAL_SIZE_CATEGORY = FileIcesSpeciesList.COLUMN_COMMERCIAL_SIZE_CATEGORY;
    public static final String COLUMN_SUBSAMPLING_CATEGORY = FileIcesSpeciesList.COLUMN_SUBSAMPLING_CATEGORY;
    public static final String COLUMN_SEX = FileIcesSpeciesList.COLUMN_SEX;
    public static final String COLUMN_INDIVIDUAL_SEX = "individual_sex";
    public static final String COLUMN_LENGTH_CLASS = "length_class";
    public static final String COLUMN_NUMBER_AT_LENGTH = "number_at_length";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "file_ices_species_length_seq")
    @SequenceGenerator(name = "file_ices_species_length_seq", sequenceName="file_ices_species_length_seq")
    private Integer id;

    @Column(nullable = false, length = 2, name = COLUMN_SAMPLING_TYPE)
    private String samplingType;

    @Column(nullable = false, length = 3, name = COLUMN_LANDING_COUNTRY)
    private String landingCountry;

    @Column(nullable = false, length = 3, name = COLUMN_VESSEL_FLAG_COUNTRY)
    private String vesselFlagCountry;

    @Column(nullable = false, scale = 4, name = COLUMN_YEAR)
    private Integer year;

    @Column(nullable = false, name = COLUMN_PROJECT)
    private String project;

    @Column(nullable = false, name = COLUMN_TRIP_CODE, length = 50)
    private String tripCode;

    @Column(name = COLUMN_STATION_NUMBER, scale=6)
    private Integer stationNumber;

    // TODO: reduce length to 20 (or 4 ?)
    @Column(nullable = false, length = 100, name = COLUMN_SPECIES)
    private String species;

    @Column(nullable = false, length= 25, name = COLUMN_CATCH_CATEGORY)
    private String catchCategory;

    @Column(nullable = false, length = 25, name = COLUMN_LANDING_CATEGORY)
    private String landingCategory;

    @Column(length = 25, name = COLUMN_COMMERCIAL_SIZE_CATEGORY_SCALE)
    private String commercialCategoryScale;

    @Column(length = 25, name = COLUMN_COMMERCIAL_SIZE_CATEGORY)
    private String commercialCategory;

    @Column(length = 25, name = COLUMN_SUBSAMPLING_CATEGORY)
    private String subsamplingCategory;

    @Column(name = COLUMN_SEX , length=1)
    private String sex;

    @Column(name = COLUMN_INDIVIDUAL_SEX , length=1)
    private String individualSex;

    @Column(scale = 5, name = COLUMN_LENGTH_CLASS)
    private Integer lengthClass; // in mm

    @Column(scale = 5, name = COLUMN_NUMBER_AT_LENGTH)
    private Integer numberAtLength;
}
