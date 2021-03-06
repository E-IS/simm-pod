package net.sumaris.core.config;

/*-
 * #%L
 * SUMARiS :: Sumaris Core Shared
 * $Id:$
 * $HeadURL:$
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

import net.sumaris.core.dao.technical.hibernate.spatial.HSQLSpatialDialect;
import org.nuiton.config.ConfigOptionDef;
import org.nuiton.version.Version;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import static org.nuiton.i18n.I18n.n;

/**
 * All application configuration options.
 *
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>
 * @since 1.0
 */
public enum SumarisConfigurationOption implements ConfigOptionDef {

    // ------------------------------------------------------------------------//
    // -- READ-ONLY OPTIONS ---------------------------------------------------//
    // ------------------------------------------------------------------------//

    APP_NAME(
            "sumaris.name",
            n("sumaris.config.option.app.name.description"),
            "SUMARiS",
            String.class),
    VERSION(
            "sumaris.version",
            n("sumaris.config.option.app.version.description"),
            "1.0.0",
            Version.class),

    BASEDIR(
            "sumaris.basedir",
            n("sumaris.config.option.basedir.description"),
            "${user.home}/.${sumaris.name}",
            File.class),

    DATA_DIRECTORY(
            "sumaris.data.directory",
            n("sumaris.config.option.data.directory.description"),
            "${sumaris.basedir}/data",
            File.class),

    I18N_DIRECTORY(
            "sumaris.i18n.directory",
            n("sumaris.config.option.i18n.directory.description"),
            "${sumaris.basedir}/i18n",
            File.class),

    TMP_DIRECTORY(
            "sumaris.tmp.directory",
            n("sumaris.config.option.tmp.directory.description"),
            "${sumaris.data.directory}/temp",
            File.class),

    DB_DIRECTORY(
            "sumaris.persistence.db.directory",
            n("sumaris.config.option.persistence.db.directory.description"),
            "${sumaris.data.directory}/db",
            File.class),

    DB_ATTACHMENT_DIRECTORY(
            "sumaris.persistence.db.attachment.directory",
            n("sumaris.config.option.persistence.db.attachment.directory.description"),
            "${sumaris.data.directory}/meas_files",
            File.class),

    DB_NAME(
            "sumaris.persistence.db.name",
            n("sumaris.config.option.persistence.db.name.description"),
            "sumaris",
            String.class),

    DB_HOST(
            "sumaris.persistence.db.host",
            n("sumaris.config.option.persistence.db.host.description"),
            "",
            String.class),

    DB_PORT(
            "sumaris.persistence.db.port",
            n("sumaris.config.option.persistence.db.port.description"),
            "",
            String.class),

    DB_VALIDATION_QUERY(
            "sumaris.persistence.db.validation-query",
            n("sumaris.persistence.db.validation-query.description"),
            "SELECT COUNT(*) FROM SYSTEM_VERSION",
            String.class),

    DB_CREATE_SCRIPT_PATH(
            "sumaris.persistence.db.script",
            n("sumaris.config.option.db.script.description"),
            "classpath:net/sumaris/core/db/changelog/sumaris.script",
            String.class,
            false),

    DB_TIMEZONE(
            "sumaris.persistence.db.timezone",
            n("sumaris.config.option.db.timezone.description"),
            "${user.timezone}",
            String.class,
            false),

    DATASOURCE_TYPE(
            "sumaris.persistence.datasource.type",
            n("sumaris.persistence.datasource.type.description"),
            "local",
            String.class),

    DATASOURCE_JNDI_NAME(
            "sumaris.persistence.jndi-name",
            n("sumaris.config.option.persistence.jndi-name.description"),
            "sumaris-ds",
            String.class),

    JDBC_USERNAME(
            "spring.datasource.username",
            n("sumaris.config.option.spring.datasource.username.description"),
            "sa",
            String.class),

    JDBC_PASSWORD(
            "spring.datasource.password",
            n("sumaris.config.option.spring.datasource.password.description"),
            "",
            String.class),

    JDBC_URL(
            "spring.datasource.url",
            n("sumaris.config.option.spring.datasource.url.description"),
            "jdbc:hsqldb:file:${sumaris.persistence.db.directory}/${sumaris.persistence.db.name}",
            String.class, false),

    JDBC_CATALOG(
            "spring.jpa.properties.hibernate.default_catalog",
            n("sumaris.config.option.hibernate.default_catalog.description"),
            "PUBLIC",
            String.class),

    JDBC_SCHEMA(
            "spring.jpa.properties.hibernate.default_schema",
            n("sumaris.config.option.hibernate.default_schema.description"),
            "PUBLIC",
            String.class),

    JDBC_DRIVER(
            "spring.datasource.driver-class-name",
            n("sumaris.config.option.spring.datasource.driver-class-name.description"),
            "org.hsqldb.jdbc.JDBCDriver",
            Class.class),

    JDBC_BATCH_SIZE(
            "sumaris.persistence.jdbc.batch-size",
            n("sumaris.config.option.persistence.jdbc.batch-size.description"),
            "15",
            Integer.class),

    HIBERNATE_DIALECT(
            "spring.jpa.database-platform",
            n("sumaris.config.option.spring.jpa.database-platform.description"),
            HSQLSpatialDialect.class.getName(),
            Class.class),

    HIBERNATE_ENTITIES_PACKAGE(
            "sumaris.persistence.hibernate.entities.package",
            n("sumaris.config.option.persistence.hibernate.entities.package.description"),
            "net.sumaris.core.model",
            Class.class),

    DEBUG_ENTITY_LOAD(
            "sumaris.persistence.hibernate.load.debug",
            n("sumaris.config.option.persistence.hibernate.load.debug.description"),
            Boolean.FALSE.toString(),
            boolean.class),

    SITE_URL(
            "sumaris.site.url",
            n("sumaris.config.option.site.url.description"),
            "https://www.sumaris.net",
            URL.class),

    SITE_DOC_URL(
            "sumaris.site.doc.url",
            n("sumaris.config.option.site.doc.url.description"),
            "http://doc.e-is.pro/sumaris",
            URL.class),

    ORGANIZATION_NAME(
            "sumaris.organizationName",
            n("sumaris.config.option.organizationName.description"),
            "SUMARiS consortium, E-IS.pro",
            String.class),

    INCEPTION_YEAR(
            "sumaris.inceptionYear",
            n("sumaris.config.option.inceptionYear.description"),
            "2011",
            Integer.class),

    // ------------------------------------------------------------------------//
    // -- DATA CONSTANTS --------------------------------------------------//
    // ------------------------------------------------------------------------//

    STATUS_ID_TEMPORARY(
            "sumaris.enumeration.StatusId.TEMPORARY",
            n("sumaris.enumeration.StatusId.TEMPORARY.description"),
            "2",
            String.class,
            false),

    STATUS_ID_ENABLE(
            "sumaris.enumeration.StatusId.ENABLE",
            n("sumaris.enumeration.StatusId.ENABLE.description"),
            "1",
            String.class,
            false),

    UNIT_ID_NONE(
            "sumaris.enumeration.UnitId.NONE",
            n("sumaris.enumeration.UnitId.NONE.description"),
            "0",
            Integer.class,
            false),

    MATRIX_ID_INDIVIDUAL(
            "sumaris.enumeration.MatrixId.INDIVIDUAL",
            n("sumaris.enumeration.MatrixId.INDIVIDUAL.description"),
            "2",
            Integer.class,
            false),

    IMPORT_NB_YEARS_DATA_HISTORY (
            "sumaris.synchro.import.nbYearDataHistory",
            n("sumaris.config.option.synchro.import.nbYearDataHistory.description"),
            "2",
            Integer.class,
            false),

    IMPORT_DATA_MAX_ROOT_ROW_COUNT(
            "sumaris.synchro.import.data.maxRootRowCount",
            n("sumaris.config.option.synchro.import.data.maxRootRowCount.description"),
            "-1",
            Integer.class,
            false),

    EXPORT_DATA_UPDATE_DATE_DELAY(
            "sumaris.synchro.export.updateDate.offset",
            n("sumaris.config.option.synchro.export.data.updateDate.offset.description"),
            String.valueOf(5 * 60), /* 5 min */
            Integer.class,
            false),

    EXPORT_DATA_UPDATE_DATE_SHORT_DELAY(
            "sumaris.synchro.export.updateDate.offset.short",
            n("sumaris.config.option.synchro.export.data.updateDate.offset.short.description"),
            String.valueOf(30), /* 30 sec */
            Integer.class,
            false),

    IMPORT_REFERENTIAL_UPDATE_DATE_OFFSET(
            "sumaris.synchro.import.updateDate.offset",
            n("sumaris.config.option.synchro.import.referential.updateDate.offset.description"),
            String.valueOf(-60), /* -1 min */
            Integer.class,
            false),

    IMPORT_REFERENTIAL_STATUS_INCLUDES(
            "sumaris.synchro.import.referential.status.includes",
            n("sumaris.config.option.synchro.import.referential.status.includes.description"),
            null, /* all */
            String.class,
            false),

    SYNCHRO_PROGRAM_CODES_INCLUDES(
            "sumaris.synchro.program.codes",
            n("sumaris.config.option.synchro.program.codes.description"),
            "",
            String.class,
            false),

    TIMEZONE(
            "sumaris.timezone",
            n("sumaris.config.option.timezone.description"),
            "",
            String.class,
            false),

    // ------------------------------------------------------------------------//
    // -- READ-WRITE OPTIONS --------------------------------------------------//
    // ------------------------------------------------------------------------//

    DB_BACKUP_DIRECTORY(
            "sumaris.persistence.db.backup.directory",
            n("sumaris.config.option.persistence.db.backup.directory.description"),
            "${sumaris.data.directory}/db-backup",
            File.class,
            false),
    HIBERNATE_SHOW_SQL(
            "spring.jpa.show-sql",
            n("sumaris.config.option.spring.jpa.show-sql.description"),
            Boolean.FALSE.toString(),
            boolean.class,
            false),

    HIBERNATE_USE_SQL_COMMENT(
            "sumaris.persistence.hibernate.useSqlComment",
            n("sumaris.config.option.persistence.hibernate.useSqlComment.description"),
            Boolean.FALSE.toString(),
            boolean.class,
            false),

    HIBERNATE_FORMAT_SQL(
            "sumaris.persistence.hibernate.formatSql",
            n("sumaris.config.option.persistence.hibernate.formatSql.description"),
            Boolean.FALSE.toString(),
            boolean.class,
            false),

    HIBERNATE_SECOND_LEVEL_CACHE(
            "sumaris.persistence.hibernate.useSecondLevelCache",
            n("sumaris.config.option.persistence.hibernate.useSecondLevelCache.description"),
            Boolean.FALSE.toString(),
            boolean.class,
            false),

    HIBERNATE_CLIENT_QUERIES_FILE(
            "sumaris.persistence.hibernate.queriesFile",
            n("sumaris.config.option.persistence.hibernate.queriesFile.description"),
            "queries-failsafe.hbm.xml",
            String.class,
            false),

    LIQUIBASE_RUN_AUTO(
            "spring.liquibase.enabled",
            n("sumaris.config.option.liquibase.should.run.description"),
            Boolean.TRUE.toString(),
            boolean.class,
            false),

    LIQUIBASE_RUN_COMPACT(
            "spring.liquibase.compact.enabled",
            n("sumaris.config.option.liquibase.should.compact.description"),
            Boolean.FALSE.toString(),
            boolean.class,
            false),

    LIQUIBASE_CHANGE_LOG_PATH(
            "spring.liquibase.change-log",
            n("sumaris.config.option.liquibase.changelog.path.description"),
            "classpath:net/sumaris/core/db/changelog/db-changelog-master.xml",
            String.class,
            false),

    LIQUIBASE_DIFF_TYPES(
            "sumaris.persistence.liquibase.diff.types",
            n("sumaris.config.option.liquibase.diff.types.description"),
            null,
            String.class,
            false),

    LIQUIBASE_OUTPUT_FILE(
            "sumaris.persistence.liquibase.output.file",
            n("sumaris.config.option.liquibase.output.file.description"),
            null,
            File.class,
            false),

    LIQUIBASE_FORCE_OUTPUT_FILE(
            "sumaris.persistence.liquibase.output.force",
            n("sumaris.config.option.liquibase.output.force.description"),
            Boolean.FALSE.toString(),
            Boolean.class,
            false),

    CSV_SEPARATOR(
            "sumaris.csv.separator",
            n("sumaris.config.option.csv.separator.description"),
            ";",
            String.class,
            false),
    VALUE_SEPARATOR(
            "sumaris.value.separator",
            n("sumaris.config.option.value.separator.description"),
            ",",
            String.class,
            false),
    ATTRIBUTE_SEPARATOR(
            "sumaris.attribute.separator",
            n("sumaris.config.option.attribute.separator.description"),
            ".",
            String.class,
            false),

    /*
    * Application options
    */

    I18N_LOCALE(
            "sumaris.i18n.locale",
            n("sumaris.config.option.i18n.locale.description"),
            Locale.FRANCE.getCountry(),
            Locale.class,
            false),

    LAUNCH_MODE(
            "sumaris.launch.mode",
            n("sumaris.config.option.launch.mode.description"),
            LaunchModeEnum.development.name(),
            String.class),

    DEFAULT_QUALITY_FLAG(
            "sumaris.persistence.qualityFlagId.default",
            n("sumaris.config.option.persistence.qualityFlagId.default.description"),
            String.valueOf(0),
            Integer.class),

    SEQUENCE_START_WITH(
            "sumaris.persistence.sequence.startWith",
            n("sumaris.config.option.persistence.sequence.startWith.description"),
            String.valueOf(1),
            Integer.class),

    SEQUENCE_INCREMENT(
            "sumaris.persistence.sequence.increment",
            n("sumaris.config.option.persistence.sequence.increment.description"),
            null, // null as default to let Hibernate use allocationSize in model (@see javax.persistence.SequenceGenerator.allocationSize)
            Integer.class),

    SEQUENCE_SUFFIX(
            "sumaris.persistence.sequence.suffix",
            n("sumaris.config.option.persistence.sequence.suffix.description"),
            "_SEQ",
            String.class),

    INIT_STATISTICAL_RECTANGLES(
            "sumaris.persistence.init.statisticalRectangles",
            n("sumaris.config.option.persistence.init.statisticalRectangles.description"),
            Boolean.TRUE.toString(),
            Boolean.class),

    PRESERVE_HISTORICAL_MEASUREMENTS(
            "sumaris.persistence.preserve.historicalMeasurements",
            n("sumaris.config.option.persistence.preserve.historicalMeasurements.description"),
            Boolean.FALSE.toString(),
            Boolean.class),

    ENABLE_BATCH_HASH_OPTIMIZATION(
            "sumaris.persistence.batch.hashOptimization",
            n("sumaris.config.option.persistence.batch.hashOptimization.description"),
            Boolean.FALSE.toString(),
            Boolean.class),

    ENABLE_SAMPLE_HASH_OPTIMIZATION(
            "sumaris.persistence.sample.hashOptimization",
            n("sumaris.config.option.persistence.sample.hashOptimization.description"),
            Boolean.FALSE.toString(),
            Boolean.class)

    ;

    /** Configuration key. */
    private final String key;

    /** I18n key of option description */
    private final String description;

    /** Type of option */
    private final Class<?> type;

    /** Default value of option. */
    private String defaultValue;

    /** Flag to not keep option value on disk */
    private boolean isTransient;

    /** Flag to not allow option value modification */
    private boolean isFinal;

    SumarisConfigurationOption(String key,
                               String description,
                               String defaultValue,
                               Class<?> type,
                               boolean isTransient) {
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
        this.type = type;
        this.isTransient = isTransient;
        this.isFinal = isTransient;
    }

    SumarisConfigurationOption(String key,
                               String description,
                               String defaultValue,
                               Class<?> type) {
        this(key, description, defaultValue, type, true);
    }

    /** {@inheritDoc} */
    @Override
    public String getKey() {
        return key;
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getType() {
        return type;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTransient() {
        return isTransient;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinal() {
        return isFinal;
    }

    /** {@inheritDoc} */
    @Override
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public void setTransient(boolean newValue) {
        // not used
    }

    /** {@inheritDoc} */
    @Override
    public void setFinal(boolean newValue) {
        // not used
    }
}
