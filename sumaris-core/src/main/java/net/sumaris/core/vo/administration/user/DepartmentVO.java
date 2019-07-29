package net.sumaris.core.vo.administration.user;

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
import lombok.EqualsAndHashCode;
import net.sumaris.core.model.administration.user.Department;
import net.sumaris.core.vo.referential.ReferentialVO;

import java.util.Date;

@Data
@EqualsAndHashCode
public class DepartmentVO extends ReferentialVO {

    public static final String PROPERTY_HAS_LOGO = "hasLogo";
    public static final String PROPERTY_LOGO = "logo";

    private Integer id;
    private Date updateDate;
    private Date creationDate;
    private String label;
    private String name;
    private Integer statusId;
    private String siteUrl;

    @EqualsAndHashCode.Exclude
    private boolean hasLogo;

    @EqualsAndHashCode.Exclude
    private String logo;

    public DepartmentVO() {
        this.setEntityName(Department.class.getSimpleName());
    }
}
