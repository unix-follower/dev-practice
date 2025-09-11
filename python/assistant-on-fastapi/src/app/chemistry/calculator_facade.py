from rdkit import Chem
from rdkit.Chem import Descriptors

from app.models.chemistry.req.molar_mass import CalculateMolarMassReq
from app.models.chemistry.req.mole import CalculateMoleReq
from app.models.chemistry.resp.calculator import CalculatorResp


class CalculatorApiFacade:
    @staticmethod
    async def calculate_molar_mass(request: CalculateMolarMassReq):
        mol = Chem.MolFromSmiles(request.smiles)
        molecular_weight = Descriptors.MolWt(mol)
        return CalculatorResp.model_construct(result=molecular_weight, result_scientific_notation=molecular_weight)

    @staticmethod
    async def calculate_mole(request: CalculateMoleReq):
        smiles = request.smiles
        if smiles is None:
            moles = request.mass / request.molecular_weight
            return CalculatorResp.model_construct(result=moles, result_scientific_notation=moles)

        mol = Chem.MolFromSmiles(smiles)
        molecular_weight = Descriptors.MolWt(mol)
        moles = request.mass / molecular_weight
        return CalculatorResp.model_construct(result=moles, result_scientific_notation=moles)
