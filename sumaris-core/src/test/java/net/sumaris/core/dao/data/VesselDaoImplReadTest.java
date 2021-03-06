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

import net.sumaris.core.dao.AbstractDaoTest;
import net.sumaris.core.dao.DatabaseResource;
import net.sumaris.core.dao.technical.SortDirection;
import net.sumaris.core.vo.data.VesselFeaturesVO;
import net.sumaris.core.vo.data.VesselVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author peck7 on 06/11/2019.
 */
public class VesselDaoImplReadTest extends AbstractDaoTest {

    /** Logger. */
    private static final Logger log =
        LoggerFactory.getLogger(VesselDaoImplReadTest.class);

    @ClassRule
    public static final DatabaseResource dbResource = DatabaseResource.readDb();

    @Autowired
    private VesselDao dao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setCommitOnTearDown(false); // this is need because of delete test
    }

    @Test
    public void findByFilter() {

        List<VesselVO> result = dao.findByFilter(null, 0, 10, VesselVO.Fields.FEATURES + "." + VesselFeaturesVO.Fields.EXTERIOR_MARKING, SortDirection.ASC);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        VesselVO vessel1 = result.get(0);
        Assert.assertEquals(1, vessel1.getId().intValue());
        Assert.assertEquals("CN851751", vessel1.getFeatures().getExteriorMarking());
        Assert.assertEquals("FRA000851751", vessel1.getRegistration().getRegistrationCode());
        Assert.assertNotNull(vessel1.getFeatures().getBasePortLocation());
        Assert.assertEquals(10, vessel1.getFeatures().getBasePortLocation().getId().intValue());
        Assert.assertNotNull(vessel1.getRegistration().getRegistrationLocation());
        Assert.assertEquals(1, vessel1.getRegistration().getRegistrationLocation().getId().intValue());
        VesselVO vessel2 = result.get(1);
        Assert.assertEquals(2, vessel2.getId().intValue());
        Assert.assertEquals("CN851769", vessel2.getFeatures().getExteriorMarking());
        Assert.assertNotNull(vessel2.getFeatures().getBasePortLocation());
        Assert.assertEquals(10, vessel2.getFeatures().getBasePortLocation().getId().intValue());
        Assert.assertNull(vessel2.getRegistration());
    }

    @Test
    public void countByFilter() {

        Long count = dao.countByFilter(null);

        Assert.assertNotNull(count);
        Assert.assertEquals(2L, count.longValue());
    }
}
