package net.sumaris.core.model.referential.taxon;

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
import net.sumaris.core.model.referential.IItemReferentialEntity;
import net.sumaris.core.model.referential.Status;

import javax.persistence.*;
import java.util.Date;

/**
 * Classe regroupant les taxons. Il s'agit en fait d'une combinaison entre un rang taxinomique, un nom de taxon, un code et éventuellement un auteur et une date.
 * Cette classe regroupe les taxons valides, provisoires, obsolètes, virtuels et les synonymes.
 * On garde l'historique du passage en taxon valide, puis du passage en synonyme (date de fin référent).
 */
@Data
@Entity
@Table(name = "taxon_name")
public class TaxonName implements IItemReferentialEntity {

    public static final String PROPERTY_IS_REFERENT = "isReferent";
    public static final String PROPERTY_REFERENCE_TAXON = "referenceTaxon";
    public static final String PROPERTY_PARENT_TAXON_NAME = "parentTaxonName";
    public static final String PROPERTY_TAXONOMIC_LEVEL = "taxonomicLevel";

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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

    @Column(length = LENGTH_LABEL)
    private String label;

    @Column(nullable = false, length = LENGTH_NAME)
    private String name;

    private String description;

    @Column(length = LENGTH_COMMENTS)
    private String comments;

    @Column(name = "complete_name", length = LENGTH_NAME)
    private String completeName;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(nullable = false, name = "is_referent")
    private Boolean isReferent;

    @Column(nullable = false, name = "is_naming")
    private Boolean isNaming;

    @Column(nullable = false, name = "is_virtual")
    private Boolean isVirtual;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ReferenceTaxon.class)
    @JoinColumn(name = "reference_taxon_fk", nullable = false)
    private ReferenceTaxon referenceTaxon;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TaxonomicLevel.class)
    @JoinColumn(name = "taxonomic_level_fk", nullable = false)
    private TaxonomicLevel taxonomicLevel;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TaxonName.class)
    @JoinColumn(name = "parent_taxon_name_fk")
    private TaxonName parentTaxonName;

}
