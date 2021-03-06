package net.sumaris.core.service.data;

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

import net.sumaris.core.dao.DatabaseFixtures;
import net.sumaris.core.dao.DatabaseResource;
import net.sumaris.core.service.AbstractServiceTest;
import net.sumaris.core.service.referential.PmfmService;
import net.sumaris.core.vo.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OperationServiceWriteTest extends AbstractServiceTest {

    @ClassRule
    public static final DatabaseResource dbResource = DatabaseResource.writeDb();

    @Autowired
    private TripService tripService;

    @Autowired
    private OperationService service;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private PmfmService pmfmService;


    private TripVO parent;

    private DatabaseFixtures fixtures;

    @Before
    public void setUp() {
        this.fixtures = dbResource.getFixtures();
        this.parent = tripService.get(fixtures.getTripId(0));
        Assume.assumeNotNull(this.parent);
    }

    @Test
    @Ignore
    // Ignore because delete() is call before get()
    public void get() {
        OperationVO vo = service.get(1);
        Assert.assertNotNull(vo);
        Assert.assertNotNull(vo.getId());
    }

    @Test
    public void saveAndGet() {
        OperationVO vo = createOperation();
        int batchCount = countBatches(vo);
        int sampleCount = countSamples(vo);

        // Save
        OperationVO savedVo = service.save(vo);
        Assert.assertNotNull(savedVo.getId());
        Assert.assertNotNull(savedVo.getQualityFlagId());

        // Full reload
        OperationVO reloadedVo = service.get(savedVo.getId());
        Assert.assertNotNull(reloadedVo);

        // Check vessel position: Should NOT be loaded in VO
        Assert.assertEquals(0, CollectionUtils.size(reloadedVo.getPositions()));

        // Check measurements
        {
            // Should NOT be loaded in VO
            Assert.assertEquals(0, CollectionUtils.size(reloadedVo.getMeasurements()));
            List<MeasurementVO> reloadMeasurements = measurementService.getOperationVesselUseMeasurements(savedVo.getId());
            Assert.assertEquals(CollectionUtils.size(savedVo.getMeasurements()), CollectionUtils.size(reloadMeasurements));
        }

        // Check samples
        {
            // Should NOT be loaded in OperationVO
            Assert.assertEquals(0, CollectionUtils.size(reloadedVo.getSamples()));
            List<SampleVO> reloadSamples = sampleService.getAllByOperationId(savedVo.getId());
            Assert.assertEquals(sampleCount, CollectionUtils.size(reloadSamples));
        }

        // Check batches
        {
            // Should NOT be loaded in OperationVO
            Assert.assertEquals(0, CollectionUtils.size(reloadedVo.getCatchBatch()));
            List<BatchVO> reloadBatches = batchService.getAllByOperationId(savedVo.getId());
            Assert.assertEquals(batchCount, CollectionUtils.size(reloadBatches));
        }
    }

    @Test
    public void delete() {
        service.delete(fixtures.getTripId(0));
    }


    @Test
    public void deleteAfterCreate() {
        OperationVO savedVO = null;
        try {
            savedVO = service.save(createOperation());
            Assume.assumeNotNull(savedVO);
            Assume.assumeNotNull(savedVO.getId());
        }
        catch(Exception e) {
            Assume.assumeNoException(e);
        }

        service.delete(savedVO.getId());
    }


    /* -- Protected -- */

    protected OperationVO createOperation() {
        return DataTestUtils.createOperation(fixtures, pmfmService, parent);
    }


    protected int countBatches(OperationVO vo) {
        if (vo.getCatchBatch() == null) return 0;
        return countBatches(vo.getCatchBatch().getChildren()) + 1;
    }

    protected int countBatches(List<BatchVO> vos) {
        if (CollectionUtils.isEmpty(vos)) return 0;

        int count = CollectionUtils.size(vos);
        for (BatchVO b : vos) {
            count += countBatches(b.getChildren());
        }
        return count;
    }

    protected int countSamples(OperationVO vo) {
        return countSamples(vo.getSamples());
    }

    protected int countSamples(List<SampleVO> vos) {
        if (CollectionUtils.isEmpty(vos)) return 0;

        int count = CollectionUtils.size(vos);
        for (SampleVO b : vos) {
            count += countSamples(b.getChildren());
        }
        return count;
    }
}
