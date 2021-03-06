package net.sumaris.core.extraction.vo;

/*-
 * #%L
 * SUMARiS:: Core Extraction
 * %%
 * Copyright (C) 2018 - 2019 SUMARiS Consortium
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

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>*
 */
public enum ExtractionRawFormatEnum {

    RDB("TR", "HH", "SL", "HL"),
    COST ("TR", "HH", "SL", "HL"),
    FREE ("TR", "HH", "SL", "HL"),
    SURVIVAL_TEST("TR", "HH", "SL", "HL", "ST", "RL")
    ;

    private String[] sheetNames;

    ExtractionRawFormatEnum(String... sheetNames) {
        this.sheetNames = sheetNames;
    }
    ExtractionRawFormatEnum() {
        this.sheetNames = null;
    }

    public String[] getSheetNames() {
        return sheetNames;
    }

    public static Optional<ExtractionRawFormatEnum> fromString(@Nullable String value) {
        if (value == null) return Optional.empty();
        try {
            return Optional.of(valueOf(value.toUpperCase()));
        }
        catch(IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
