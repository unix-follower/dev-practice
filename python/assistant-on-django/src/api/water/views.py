from rest_framework.response import Response
from rest_framework.views import APIView

from src.api.common.serializers import ElementCalcResultResponseSerializer
from src.component import water_calculator

_MILLILITERS = "milliliters"


class WaterView(APIView):
    def get(self, request):
        milliliters = float(request.query_params[_MILLILITERS])
        result = water_calculator.calculate_molecules(milliliters)
        response_serializer = ElementCalcResultResponseSerializer(instance=result)
        return Response(response_serializer.data)
