/*
 * #%L
 * SUMARiS
 * %%
 * Copyright (C) 2019 SUMARiS Consortium
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

package net.sumaris.rdf.service;

import net.sumaris.rdf.config.RdfConfiguration;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Abstract class for unit test on services.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestConfiguration.class})
@TestPropertySource(locations="classpath:sumaris-core-rdf-test.properties")
public class AbstractServiceTest {

	/** Logger. */
	private static final Logger log =
			LoggerFactory.getLogger(AbstractServiceTest.class);

	@Autowired
	protected RdfConfiguration config;

	/* -- Internal method -- */

	protected RdfConfiguration getConfig() {
		return config;
	}


}
