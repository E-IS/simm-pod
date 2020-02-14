package net.sumaris.core.model.referential.transcribing;

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
import lombok.experimental.FieldNameConstants;
import net.sumaris.core.model.referential.IItemReferentialEntity;
import net.sumaris.core.model.referential.ObjectType;
import net.sumaris.core.model.referential.Status;

import javax.persistence.*;
import java.util.Date;

@Data
@FieldNameConstants
@Entity
@Table(name = "transcribing_item_type")
public class TranscribingItemType implements IItemReferentialEntity  {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "TRANSCRIBING_ITEM_TYPE_SEQ")
    @SequenceGenerator(name = "TRANSCRIBING_ITEM_TYPE_SEQ", sequenceName="TRANSCRIBING_ITEM_TYPE_SEQ", allocationSize = SEQUENCE_ALLOCATION_SIZE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_fk", nullable = false)
    private Status status;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(nullable = false, length = LENGTH_LABEL)
    private String label;

    @Column(nullable = false, length = LENGTH_NAME)
    private String name;

    private String description;

    @Column(length = LENGTH_COMMENTS)
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ObjectType.class)
    @JoinColumn(name = "object_type_fk")
    private ObjectType objectType;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TranscribingSystem.class)
    @JoinColumn(name = "transcribing_system_fk")
    private TranscribingSystem system;
}
