package net.sumaris.core.model.data;

/*-
 * #%L
 * SUMARiS:: Core shared
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

import net.sumaris.core.dao.technical.model.IEntity;

import java.io.Serializable;

public interface IWithRecorderDepartmentEntity<ID extends Serializable, P extends IEntity<Integer>> extends IEntity<ID> {

    String PROPERTY_RECORDER_DEPARTMENT = "recorderDepartment";

    P getRecorderDepartment();

    void setRecorderDepartment(P recorderDepartment);
}