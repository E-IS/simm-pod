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

import net.sumaris.core.dao.technical.model.IUpdateDateEntityBean;
import net.sumaris.core.model.administration.user.Department;
import net.sumaris.core.model.referential.QualityFlag;

import java.io.Serializable;
import java.util.Date;

public interface IDataEntity<ID extends Serializable>
        extends Serializable, IUpdateDateEntityBean<ID, Date>,
        IWithRecorderDepartmentEntity<ID, Department> {

    int SEQUENCE_ALLOCATION_SIZE = 50;

    int LENGTH_COMMENTS = 2000;

    Date getControlDate();

    void setControlDate(Date controlDate);

    Date getQualificationDate();

    void setQualificationDate(Date qualificationDate);

    QualityFlag getQualityFlag();

    void setQualityFlag(QualityFlag qualityFlag);

    String getQualificationComments();

    void setQualificationComments(String qualificationComments);
}
