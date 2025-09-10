from fastapi import APIRouter

from src.app.chemistry.calculator_facade import CalculatorApiFacade
from src.app.models.chemistry.calculator_requests import ChemistryCalculatorReq

router = APIRouter(prefix="/api/v1/chemistry/calculator")


@router.post("/molar-mass")
async def calculate_molar_mass(request: ChemistryCalculatorReq):
    facade = CalculatorApiFacade()
    return await facade.calculate_molar_mass(request)
