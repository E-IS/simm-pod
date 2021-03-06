package net.sumaris.core.extraction.dao.trip.cost;

/*-
 * #%L
 * SUMARiS:: Core Extraction
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

import com.google.common.base.Preconditions;
import net.sumaris.core.extraction.dao.technical.XMLQuery;
import net.sumaris.core.extraction.dao.trip.rdb.ExtractionRdbTripDaoImpl;
import net.sumaris.core.extraction.vo.ExtractionFilterVO;
import net.sumaris.core.extraction.vo.trip.ExtractionTripFilterVO;
import net.sumaris.core.extraction.vo.trip.cost.ExtractionCostTripVersion;
import net.sumaris.core.extraction.vo.trip.rdb.ExtractionRdbTripContextVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>
 */
@Repository("extractionCostTripDao")
@Lazy
public class ExtractionCostTripDaoImpl<C extends ExtractionRdbTripContextVO> extends ExtractionRdbTripDaoImpl<C> implements ExtractionCostTripDao {

    private static final Logger log = LoggerFactory.getLogger(ExtractionCostTripDaoImpl.class);

    private static final String XML_QUERY_COST_PATH = "cost/v%s/%s";
    private String version = ExtractionCostTripVersion.VERSION_1_4.getLabel();

    @Override
    public C execute(ExtractionFilterVO filter) {
        C context = super.execute(filter);

        // Override some context properties
        context.setFormatName(COST_FORMAT);
        context.setFormatVersion(version);

        return context;
    }

    /* -- protected methods -- */

    protected XMLQuery createStationQuery(C context) {

        XMLQuery xmlQuery = super.createStationQuery(context);

        // Special case for COST format:
        // - Hide GearType (not in the COST format)
        xmlQuery.setGroup("gearType", false);

        return xmlQuery;
    }

    @Override
    protected XMLQuery createSpeciesLengthQuery(C context) {
        XMLQuery xmlQuery = super.createSpeciesLengthQuery(context);

        // Special case for COST format:

        // - Hide sex columns, then replace by a new columns
        xmlQuery.setGroup("sex", false);
        xmlQuery.setGroup("lengthClass", false);
        xmlQuery.setGroup("numberAtLength", false);

        xmlQuery.injectQuery(getXMLQueryURL(context, "injectionSpeciesLengthTable"));

        return xmlQuery;
    }

    protected String getQueryFullName(C context, String queryName) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getFormatVersion());

        String versionStr = version.replaceAll("[.]", "_");
        switch (queryName) {
            case "injectionSpeciesLengthTable":
                return String.format(XML_QUERY_COST_PATH, versionStr, queryName);
            default:
                return super.getQueryFullName(context, queryName);
        }
    }
}
