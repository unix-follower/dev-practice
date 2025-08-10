package org.example.db.pubchem.fda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "food_additive_substance")
public class FoodAdditiveSubstance {
    @Id
    @Column(name = "compound_cid", nullable = false)
    private Integer compoundCid;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String synonyms;

    @Column(name = "molecular_weight")
    private Double molecularWeight;

    @Column(name = "molecular_formula")
    private String molecularFormula;

    @Column(name = "polar_area")
    private Double polarArea;

    @Column
    private Double complexity;

    @Column
    private Double xlogp;

    @Column(name = "heavy_atom_count")
    private Integer heavyAtomCount;

    @Column(name = "h_bond_donor_count")
    private Integer hBondDonorCount;

    @Column(name = "h_bond_acceptor_count")
    private Integer hBondAcceptorCount;

    @Column(name = "rotatable_bond_count")
    private Integer rotatableBondCount;

    @Column(columnDefinition = "TEXT")
    private String inchi;

    @Column(columnDefinition = "TEXT")
    private String smiles;

    @Column(name = "inchi_key", columnDefinition = "TEXT")
    private String inchiKey;

    @Column(name = "iupac_name", columnDefinition = "TEXT")
    private String iupacName;

    @Column(name = "exact_mass")
    private Double exactMass;

    @Column(name = "monoisotopic_mass")
    private Double monoisotopicMass;

    @Column
    private Integer charge;

    @Column(name = "covalent_unit_count")
    private Integer covalentUnitCount;

    @Column(name = "isotopic_atom_count")
    private Integer isotopicAtomCount;

    @Column(name = "total_atom_stereo_count")
    private Integer totalAtomStereoCount;

    @Column(name = "defined_atom_stereo_count")
    private Integer definedAtomStereoCount;

    @Column(name = "undefined_atom_stereo_count")
    private Integer undefinedAtomStereoCount;

    @Column(name = "total_bond_stereo_count")
    private Integer totalBondStereoCount;

    @Column(name = "defined_bond_stereo_count")
    private Integer definedBondStereoCount;

    @Column(name = "undefined_bond_stereo_count")
    private Integer undefinedBondStereoCount;

    @Column(name = "linked_pubchem_literature_count")
    private Integer linkedPubChemLiteratureCount;

    @Column(name = "linked_pubchem_patent_count")
    private Integer linkedPubChemPatentCount;

    @Column(name = "linked_pubchem_patent_family_count")
    private Integer linkedPubChemPatentFamilyCount;

    @Column(name = "mesh_headings", columnDefinition = "TEXT")
    private String meshHeadings;

    @Column(name = "annotation_content", columnDefinition = "TEXT")
    private String annotationContent;

    @Column(name = "annotation_type_count")
    private Integer annotationTypeCount;

    @Column(name = "linked_bioassays")
    private Integer linkedBioAssays;

    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    private LocalDate createDate;

    @Column(name = "data_source", columnDefinition = "TEXT")
    private String dataSource;

    @Column(name = "data_source_category", columnDefinition = "TEXT")
    private String dataSourceCategory;

    @Column(name = "tagged_by_pubchem", columnDefinition = "TEXT")
    private String taggedByPubChem;

    public Integer getCompoundCid() {
        return compoundCid;
    }

    public void setCompoundCid(Integer compoundCid) {
        this.compoundCid = compoundCid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public Double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(Double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public Double getPolarArea() {
        return polarArea;
    }

    public void setPolarArea(Double polarArea) {
        this.polarArea = polarArea;
    }

    public Double getComplexity() {
        return complexity;
    }

    public void setComplexity(Double complexity) {
        this.complexity = complexity;
    }

    public Double getXlogp() {
        return xlogp;
    }

    public void setXlogp(Double xlogp) {
        this.xlogp = xlogp;
    }

    public Integer getHeavyAtomCount() {
        return heavyAtomCount;
    }

    public void setHeavyAtomCount(Integer heavyAtomCount) {
        this.heavyAtomCount = heavyAtomCount;
    }

    public Integer getHBondDonorCount() {
        return hBondDonorCount;
    }

    public void setHBondDonorCount(Integer hBondDonorCount) {
        this.hBondDonorCount = hBondDonorCount;
    }

    public Integer getHBondAcceptorCount() {
        return hBondAcceptorCount;
    }

    public void setHBondAcceptorCount(Integer hBondAcceptorCount) {
        this.hBondAcceptorCount = hBondAcceptorCount;
    }

    public Integer getRotatableBondCount() {
        return rotatableBondCount;
    }

    public void setRotatableBondCount(Integer rotatableBondCount) {
        this.rotatableBondCount = rotatableBondCount;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public String getIupacName() {
        return iupacName;
    }

    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    public Double getExactMass() {
        return exactMass;
    }

    public void setExactMass(Double exactMass) {
        this.exactMass = exactMass;
    }

    public Double getMonoisotopicMass() {
        return monoisotopicMass;
    }

    public void setMonoisotopicMass(Double monoisotopicMass) {
        this.monoisotopicMass = monoisotopicMass;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getCovalentUnitCount() {
        return covalentUnitCount;
    }

    public void setCovalentUnitCount(Integer covalentUnitCount) {
        this.covalentUnitCount = covalentUnitCount;
    }

    public Integer getIsotopicAtomCount() {
        return isotopicAtomCount;
    }

    public void setIsotopicAtomCount(Integer isotopicAtomCount) {
        this.isotopicAtomCount = isotopicAtomCount;
    }

    public Integer getTotalAtomStereoCount() {
        return totalAtomStereoCount;
    }

    public void setTotalAtomStereoCount(Integer totalAtomStereoCount) {
        this.totalAtomStereoCount = totalAtomStereoCount;
    }

    public Integer getDefinedAtomStereoCount() {
        return definedAtomStereoCount;
    }

    public void setDefinedAtomStereoCount(Integer definedAtomStereoCount) {
        this.definedAtomStereoCount = definedAtomStereoCount;
    }

    public Integer getUndefinedAtomStereoCount() {
        return undefinedAtomStereoCount;
    }

    public void setUndefinedAtomStereoCount(Integer undefinedAtomStereoCount) {
        this.undefinedAtomStereoCount = undefinedAtomStereoCount;
    }

    public Integer getTotalBondStereoCount() {
        return totalBondStereoCount;
    }

    public void setTotalBondStereoCount(Integer totalBondStereoCount) {
        this.totalBondStereoCount = totalBondStereoCount;
    }

    public Integer getDefinedBondStereoCount() {
        return definedBondStereoCount;
    }

    public void setDefinedBondStereoCount(Integer definedBondStereoCount) {
        this.definedBondStereoCount = definedBondStereoCount;
    }

    public Integer getUndefinedBondStereoCount() {
        return undefinedBondStereoCount;
    }

    public void setUndefinedBondStereoCount(Integer undefinedBondStereoCount) {
        this.undefinedBondStereoCount = undefinedBondStereoCount;
    }

    public Integer getLinkedPubChemLiteratureCount() {
        return linkedPubChemLiteratureCount;
    }

    public void setLinkedPubChemLiteratureCount(Integer linkedPubChemLiteratureCount) {
        this.linkedPubChemLiteratureCount = linkedPubChemLiteratureCount;
    }

    public Integer getLinkedPubChemPatentCount() {
        return linkedPubChemPatentCount;
    }

    public void setLinkedPubChemPatentCount(Integer linkedPubChemPatentCount) {
        this.linkedPubChemPatentCount = linkedPubChemPatentCount;
    }

    public Integer getLinkedPubChemPatentFamilyCount() {
        return linkedPubChemPatentFamilyCount;
    }

    public void setLinkedPubChemPatentFamilyCount(Integer linkedPubChemPatentFamilyCount) {
        this.linkedPubChemPatentFamilyCount = linkedPubChemPatentFamilyCount;
    }

    public String getMeshHeadings() {
        return meshHeadings;
    }

    public void setMeshHeadings(String meshHeadings) {
        this.meshHeadings = meshHeadings;
    }

    public String getAnnotationContent() {
        return annotationContent;
    }

    public void setAnnotationContent(String annotationContent) {
        this.annotationContent = annotationContent;
    }

    public Integer getAnnotationTypeCount() {
        return annotationTypeCount;
    }

    public void setAnnotationTypeCount(Integer annotationTypeCount) {
        this.annotationTypeCount = annotationTypeCount;
    }

    public Integer getLinkedBioAssays() {
        return linkedBioAssays;
    }

    public void setLinkedBioAssays(Integer linkedBioAssays) {
        this.linkedBioAssays = linkedBioAssays;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceCategory() {
        return dataSourceCategory;
    }

    public void setDataSourceCategory(String dataSourceCategory) {
        this.dataSourceCategory = dataSourceCategory;
    }

    public String getTaggedByPubChem() {
        return taggedByPubChem;
    }

    public void setTaggedByPubChem(String taggedByPubChem) {
        this.taggedByPubChem = taggedByPubChem;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FoodAdditiveSubstance that)) {
            return false;
        }
        return Objects.equals(compoundCid, that.compoundCid)
            && Objects.equals(name, that.name)
            && Objects.equals(synonyms, that.synonyms)
            && Objects.equals(molecularWeight, that.molecularWeight)
            && Objects.equals(molecularFormula, that.molecularFormula)
            && Objects.equals(polarArea, that.polarArea)
            && Objects.equals(complexity, that.complexity)
            && Objects.equals(xlogp, that.xlogp)
            && Objects.equals(heavyAtomCount, that.heavyAtomCount)
            && Objects.equals(hBondDonorCount, that.hBondDonorCount)
            && Objects.equals(hBondAcceptorCount, that.hBondAcceptorCount)
            && Objects.equals(rotatableBondCount, that.rotatableBondCount)
            && Objects.equals(inchi, that.inchi)
            && Objects.equals(smiles, that.smiles)
            && Objects.equals(inchiKey, that.inchiKey)
            && Objects.equals(iupacName, that.iupacName)
            && Objects.equals(exactMass, that.exactMass)
            && Objects.equals(monoisotopicMass, that.monoisotopicMass)
            && Objects.equals(charge, that.charge)
            && Objects.equals(covalentUnitCount, that.covalentUnitCount)
            && Objects.equals(isotopicAtomCount, that.isotopicAtomCount)
            && Objects.equals(totalAtomStereoCount, that.totalAtomStereoCount)
            && Objects.equals(definedAtomStereoCount, that.definedAtomStereoCount)
            && Objects.equals(undefinedAtomStereoCount, that.undefinedAtomStereoCount)
            && Objects.equals(totalBondStereoCount, that.totalBondStereoCount)
            && Objects.equals(definedBondStereoCount, that.definedBondStereoCount)
            && Objects.equals(undefinedBondStereoCount, that.undefinedBondStereoCount)
            && Objects.equals(linkedPubChemLiteratureCount, that.linkedPubChemLiteratureCount)
            && Objects.equals(linkedPubChemPatentCount, that.linkedPubChemPatentCount)
            && Objects.equals(linkedPubChemPatentFamilyCount, that.linkedPubChemPatentFamilyCount)
            && Objects.equals(meshHeadings, that.meshHeadings)
            && Objects.equals(annotationContent, that.annotationContent)
            && Objects.equals(annotationTypeCount, that.annotationTypeCount)
            && Objects.equals(linkedBioAssays, that.linkedBioAssays)
            && Objects.equals(createDate, that.createDate)
            && Objects.equals(dataSource, that.dataSource)
            && Objects.equals(dataSourceCategory, that.dataSourceCategory)
            && Objects.equals(taggedByPubChem, that.taggedByPubChem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            compoundCid,
            name,
            synonyms,
            molecularWeight,
            molecularFormula,
            polarArea,
            complexity,
            xlogp,
            heavyAtomCount,
            hBondDonorCount,
            hBondAcceptorCount,
            rotatableBondCount,
            inchi,
            smiles,
            inchiKey,
            iupacName,
            exactMass,
            monoisotopicMass,
            charge,
            covalentUnitCount,
            isotopicAtomCount,
            totalAtomStereoCount,
            definedAtomStereoCount,
            undefinedAtomStereoCount,
            totalBondStereoCount,
            definedBondStereoCount,
            undefinedBondStereoCount,
            linkedPubChemLiteratureCount,
            linkedPubChemPatentCount,
            linkedPubChemPatentFamilyCount,
            meshHeadings,
            annotationContent,
            annotationTypeCount,
            linkedBioAssays,
            createDate,
            dataSource,
            dataSourceCategory,
            taggedByPubChem
        );
    }
}
