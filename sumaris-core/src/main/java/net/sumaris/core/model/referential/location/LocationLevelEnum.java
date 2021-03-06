package net.sumaris.core.model.referential.location;

/*-
 * #%L
 * SUMARiS:: Core
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

import java.io.Serializable;
import java.util.Arrays;

public enum LocationLevelEnum implements Serializable {


    COUNTRY(1, "Country"),
    HARBOUR(2, "Port"),
    AUCTION(3, "Auction"),
    RECTANGLE_ICES(4,"ICES_RECTANGLE"),
    RECTANGLE_CGPM_GFCM(5,"CGPM_GFCM_RECTANGLE"),
    SQUARE_10(6, "SQUARE_10"), // 10' x 10'
    SQUARE_3(7, "SQUARE_3") // 3' x 3'
    ;

    public static LocationLevelEnum valueOf(final int id) {
        return Arrays.stream(values())
                .filter(level -> level.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown LocationLevelEnum: " + id));
    }

    public static LocationLevelEnum byLabel(final String label) {
        return Arrays.stream(values())
                .filter(level -> label.equals(level.label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown LocationLevelEnum: " + label));
    }

    private int id;
    private String label;

    LocationLevelEnum(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
