package net.sumaris.core.vo.referential;

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
import net.sumaris.core.model.referential.taxon.TaxonName;

@Data
@FieldNameConstants
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TaxonNameVO extends ReferentialVO {

    private Integer referenceTaxonId;
    private Boolean isReferent;

    public TaxonNameVO() {
        this.setEntityName(TaxonName.class.getSimpleName()); // Need by client (e.f. GraphQL cache)
    }
}
