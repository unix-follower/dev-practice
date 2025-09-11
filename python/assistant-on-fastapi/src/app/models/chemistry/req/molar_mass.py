from src.app.models.common import CamelCaseDtoModel


class CalculateMolarMassReq(CamelCaseDtoModel):
    smiles: str
