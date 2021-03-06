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


import com.google.common.base.Preconditions;
import net.sumaris.core.dao.data.MeasurementDao;
import net.sumaris.core.dao.data.SaleDao;
import net.sumaris.core.model.data.IMeasurementEntity;
import net.sumaris.core.model.data.SaleMeasurement;
import net.sumaris.core.util.Beans;
import net.sumaris.core.vo.data.MeasurementVO;
import net.sumaris.core.vo.data.SaleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("saleService")
public class SaleServiceImpl implements SaleService {

	private static final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

	@Autowired
	protected SaleDao saleDao;

	@Autowired
	protected MeasurementDao measurementDao;

	@Override
	public List<SaleVO> getAllByTripId(int tripId) {
		return saleDao.getAllByTripId(tripId);
	}

	@Override
	public SaleVO get(int saleId) {
		return saleDao.get(saleId);
	}

	@Override
	public List<SaleVO> saveAllByTripId(int tripId, List<SaleVO> sources) {
		return saleDao.saveAllByTripId(tripId, sources);
	}

	@Override
	public SaleVO save(SaleVO sale) {
		Preconditions.checkNotNull(sale);
		Preconditions.checkNotNull(sale.getStartDateTime(), "Missing startDateTime");
		Preconditions.checkNotNull(sale.getSaleLocation(), "Missing saleLocation");
		Preconditions.checkNotNull(sale.getSaleLocation().getId(), "Missing saleLocation.id");
		Preconditions.checkNotNull(sale.getSaleType(), "Missing saleType");
		Preconditions.checkNotNull(sale.getSaleType().getId(), "Missing saleType.id");
		Preconditions.checkNotNull(sale.getRecorderDepartment(), "Missing sale.recorderDepartment");
		Preconditions.checkNotNull(sale.getRecorderDepartment().getId(), "Missing sale.recorderDepartment.id");

		SaleVO savedSale = saleDao.save(sale);

		// Save measurements
		if (savedSale.getMeasurementValues() != null) {
			measurementDao.saveSaleMeasurementsMap(savedSale.getId(), savedSale.getMeasurementValues());
		}
		else {
			List<MeasurementVO> measurements = Beans.getList(savedSale.getMeasurements());
			measurements.forEach(m -> fillDefaultProperties(savedSale, m, SaleMeasurement.class));
			measurements = measurementDao.saveSaleMeasurements(savedSale.getId(), measurements);
			savedSale.setMeasurements(measurements);
		}

		return savedSale;
	}

	@Override
	public List<SaleVO> save(List<SaleVO> sales) {
		Preconditions.checkNotNull(sales);

		return sales.stream()
				.map(this::save)
				.collect(Collectors.toList());
	}

	@Override
	public void delete(int id) {
		saleDao.delete(id);
	}

	@Override
	public void delete(List<Integer> ids) {
		Preconditions.checkNotNull(ids);
		ids.stream()
				.filter(Objects::nonNull)
				.forEach(this::delete);
	}

	/* -- protected methods -- */

	protected void fillDefaultProperties(SaleVO parent, MeasurementVO measurement, Class<? extends IMeasurementEntity> entityClass) {
		if (measurement == null) return;

		// Copy recorder department from the parent
		if (measurement.getRecorderDepartment() == null || measurement.getRecorderDepartment().getId() == null) {
			measurement.setRecorderDepartment(parent.getRecorderDepartment());
		}

		measurement.setEntityName(entityClass.getSimpleName());
	}
}
