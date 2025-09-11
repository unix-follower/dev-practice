from fastapi import APIRouter

from app.errors.chemistry_api_exception import ChemistryApiException
from app.errors.error_code import ErrorCode
from app.models.chemistry.req.molar_mass import CalculateMolarMassReq
from app.models.chemistry.req.mole import CalculateMoleReq
from src.app.chemistry.calculator_facade import CalculatorApiFacade

router = APIRouter(prefix="/api/v1/chemistry/calculator")


@router.post("/molar-mass")
async def calculate_molar_mass(body: CalculateMolarMassReq):
    return await CalculatorApiFacade.calculate_molar_mass(body)


@router.post("/mole")
async def calculate_mole(body: CalculateMoleReq):
    if body.smiles is None and body.mass is None and body.molecularWeight is None:
        raise ChemistryApiException("None of the params are provided.", ErrorCode.INVALID_INPUT)

    return await CalculatorApiFacade.calculate_mole(body)
