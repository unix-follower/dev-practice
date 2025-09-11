from src.app.models.common import CamelCaseDtoModel


class CalculateMoleReq(CamelCaseDtoModel):
    smiles: str | None = None
    mass: float | None = None
    molecular_weight: float | None = None
