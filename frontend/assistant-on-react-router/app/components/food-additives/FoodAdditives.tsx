import React from "react"
import { useTranslation } from "react-i18next"
import type { FoodAdditiveSubstanceResponseDto } from "~/lib/features/chemistry/food-additives/model/FoodAdditiveResponse"

function Columns() {
  const { t } = useTranslation()

  return (
    <thead>
      <tr>
        <th>{t("chemistryFoodAdditives.compoundId")}</th>
        <th>{t("chemistryFoodAdditives.name")}</th>
        <th>{t("chemistryFoodAdditives.synonyms")}</th>
        <th>{t("chemistryFoodAdditives.molecularWeight")}</th>
        <th>{t("chemistryFoodAdditives.molecularFormula")}</th>
        <th>{t("chemistryFoodAdditives.polarArea")}</th>
        <th>{t("chemistryFoodAdditives.complexity")}</th>
        <th>{t("chemistryFoodAdditives.xlogp")}</th>
        <th>{t("chemistryFoodAdditives.heavyAtomCount")}</th>
        <th>{t("chemistryFoodAdditives.hBondDonorCount")}</th>
        <th>{t("chemistryFoodAdditives.hBondAcceptorCount")}</th>
        <th>{t("chemistryFoodAdditives.rotatableBondCount")}</th>
        <th>{t("chemistryFoodAdditives.inchi")}</th>
        <th>{t("chemistryFoodAdditives.smiles")}</th>
        <th>{t("chemistryFoodAdditives.inchiKey")}</th>
        <th>{t("chemistryFoodAdditives.iupacName")}</th>
        <th>{t("chemistryFoodAdditives.exactMass")}</th>
        <th>{t("chemistryFoodAdditives.monoisotopicMass")}</th>
        <th>{t("chemistryFoodAdditives.charge")}</th>
        <th>{t("chemistryFoodAdditives.covalentUnitCount")}</th>
        <th>{t("chemistryFoodAdditives.isotopicAtomCount")}</th>
        <th>{t("chemistryFoodAdditives.totalAtomStereoCount")}</th>
        <th>{t("chemistryFoodAdditives.definedAtomStereoCount")}</th>
        <th>{t("chemistryFoodAdditives.undefinedAtomStereoCount")}</th>
        <th>{t("chemistryFoodAdditives.totalBondStereoCount")}</th>
        <th>{t("chemistryFoodAdditives.definedBondStereoCount")}</th>
        <th>{t("chemistryFoodAdditives.undefinedBondStereoCount")}</th>
        <th>{t("chemistryFoodAdditives.linkedPubChemLiteratureCount")}</th>
        <th>{t("chemistryFoodAdditives.linkedPubChemPatentCount")}</th>
        <th>{t("chemistryFoodAdditives.linkedPubChemPatentFamilyCount")}</th>
        <th>{t("chemistryFoodAdditives.meshHeadings")}</th>
        <th>{t("chemistryFoodAdditives.annotationContent")}</th>
        <th>{t("chemistryFoodAdditives.annotationTypeCount")}</th>
        <th>{t("chemistryFoodAdditives.linkedBioAssays")}</th>
        <th>{t("chemistryFoodAdditives.createDate")}</th>
        <th>{t("chemistryFoodAdditives.dataSource")}</th>
        <th>{t("chemistryFoodAdditives.dataSourceCategory")}</th>
        <th>{t("chemistryFoodAdditives.taggedByPubChem")}</th>
      </tr>
    </thead>
  )
}

interface FoodAdditivesProps {
  foodAdditives: FoodAdditiveSubstanceResponseDto
}

export default function FoodAdditives({ foodAdditives }: FoodAdditivesProps) {
  const { t } = useTranslation()

  if (!foodAdditives) {
    return <div>{t("chemistryFoodAdditives.noData")}</div>
  }

  return (
    <table>
      <Columns />
      <tbody>
        {foodAdditives.map((data) => (
          <tr key={data.compoundCid}>
            <td>{data.compoundCid}</td>
            <td>{data.name}</td>
            <td>{data.synonyms}</td>
            <td>{data.molecularWeight}</td>
            <td>{data.molecularFormula}</td>
            <td>{data.polarArea}</td>
            <td>{data.complexity}</td>
            <td>{data.xlogp}</td>
            <td>{data.heavyAtomCount}</td>
            <td>{data.hBondDonorCount}</td>
            <td>{data.hBondAcceptorCount}</td>
            <td>{data.rotatableBondCount}</td>
            <td>{data.inchi}</td>
            <td>{data.smiles}</td>
            <td>{data.inchiKey}</td>
            <td>{data.iupacName}</td>
            <td>{data.exactMass}</td>
            <td>{data.monoisotopicMass}</td>
            <td>{data.charge}</td>
            <td>{data.covalentUnitCount}</td>
            <td>{data.isotopicAtomCount}</td>
            <td>{data.totalAtomStereoCount}</td>
            <td>{data.definedAtomStereoCount}</td>
            <td>{data.undefinedAtomStereoCount}</td>
            <td>{data.totalBondStereoCount}</td>
            <td>{data.definedBondStereoCount}</td>
            <td>{data.undefinedBondStereoCount}</td>
            <td>{data.linkedPubChemLiteratureCount}</td>
            <td>{data.linkedPubChemPatentCount}</td>
            <td>{data.linkedPubChemPatentFamilyCount}</td>
            <td>{data.meshHeadings}</td>
            <td>{data.annotationContent}</td>
            <td>{data.annotationTypeCount}</td>
            <td>{data.linkedBioAssays}</td>
            <td>{data.createDate?.toString()}</td>
            <td>{data.dataSource}</td>
            <td>{data.dataSourceCategory}</td>
            <td>{data.taggedByPubChem}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
