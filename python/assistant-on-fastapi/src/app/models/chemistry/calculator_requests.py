from src.app.models.common import CamelCaseDtoModel


class ChemistryCalculatorReq(CamelCaseDtoModel):
    smiles: str
