package net.sumaris.server.http;

/*-
 * #%L
 * Quadrige3 Core :: Quadrige3 Core Shared
 * %%
 * Copyright (C) 2017 Ifremer
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

/**
 * Application specific HTTP headers
 */
public interface HttpHeaders {

    String ACCESS_CONTROL_DENY_DELETION_ENTITIES = "Access-Control-Deny-Deletion-Entities";
    String AUTHORIZATION = "Authorization";
}
