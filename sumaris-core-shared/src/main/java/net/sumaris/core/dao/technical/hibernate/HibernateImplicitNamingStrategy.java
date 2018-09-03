package net.sumaris.core.dao.technical.hibernate;

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

import org.hibernate.boot.model.naming.*;

public class HibernateImplicitNamingStrategy extends ImplicitNamingStrategyLegacyJpaImpl implements ImplicitNamingStrategy {

    private static final String FK_SUFFIX = "_fk";

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        // Use _fk
        if (source.getNature() != ImplicitJoinColumnNameSource.Nature.ELEMENT_COLLECTION
                && source.getAttributePath() != null
                && ("id".equalsIgnoreCase(source.getReferencedColumnName().getText())
                || "code".equalsIgnoreCase(source.getReferencedColumnName().getText()))) {
            String name = this.transformAttributePath(source.getAttributePath()) + FK_SUFFIX;
            return this.toIdentifier(name, source.getBuildingContext());
        }
        return super.determineJoinColumnName(source);
    }

    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        // TODO : voir si on peut faire une génération plus déterministe
        return super.determineForeignKeyName(source);
    }




}
