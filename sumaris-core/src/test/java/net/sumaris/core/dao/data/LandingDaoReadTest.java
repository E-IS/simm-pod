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

import net.sumaris.core.dao.AbstractDaoTest;
import net.sumaris.core.dao.DatabaseResource;
import net.sumaris.core.vo.data.LandingVO;
import net.sumaris.core.vo.filter.LandingFilterVO;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LandingDaoReadTest extends AbstractDaoTest{

    /** Logger. */
    private static final Logger log =
            LoggerFactory.getLogger(LandingDaoReadTest.class);

    @ClassRule
    public static final DatabaseResource dbResource = DatabaseResource.readDb();

    @Autowired
    private LandingRepository repository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setCommitOnTearDown(false); // this is need because of delete test
    }

    @Test
    public void findAll() {

        // by observed location
        {
            LandingFilterVO filter = new LandingFilterVO();
            //filter.setProgramLabel("ADAP-MER");
            filter.setObservedLocationId(1);

            List<LandingVO> vos = repository.findAll(filter);
            assertNotNull(vos);
            assertTrue(vos.size() > 0);
        }

        // by program
        {
            LandingFilterVO filter = new LandingFilterVO();
            filter.setProgramLabel("ADAP-CONTROL");

            List<LandingVO> vos = repository.findAll(filter);
            assertNotNull(vos);
            assertTrue(vos.size() > 0);
        }

    }


}
