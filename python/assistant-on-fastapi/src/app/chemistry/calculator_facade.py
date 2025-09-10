from rdkit import Chem
from rdkit.Chem import Descriptors

from src.app.models.chemistry.calculator_requests import ChemistryCalculatorReq
from src.app.models.chemistry.calculator_responses import ChemistryCalculatorResp


class CalculatorApiFacade:
    async def calculate_molar_mass(self, request: ChemistryCalculatorReq):
        mol = Chem.MolFromSmiles(request.smiles)
        molar_mass = Descriptors.MolWt(mol)
        return ChemistryCalculatorResp.model_construct(result=molar_mass)
