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
import lombok.experimental.FieldNameConstants;
import net.sumaris.core.dao.technical.model.IUpdateDateEntityBean;
import net.sumaris.core.vo.IValueObject;

import java.util.Date;
import java.util.List;

@Data
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersonVO implements IUpdateDateEntityBean<Integer, Date>, IValueObject<Integer> {

    @EqualsAndHashCode.Include
    private Integer id;
    private Date updateDate;
    private Date creationDate;

    private String firstName;
    private String lastName;
    private String email;

    private String pubkey;

    private Integer statusId;

    private DepartmentVO department;

    @EqualsAndHashCode.Exclude
    private List<String> profiles;

    // Workaround for issue see https://github.com/sumaris-net/sumaris-app/issues/3
    // TODO: remove this, later
    @EqualsAndHashCode.Exclude
    private String mainProfile;

    @EqualsAndHashCode.Exclude
    private Boolean hasAvatar;

    @EqualsAndHashCode.Exclude
    private String avatar;

}
