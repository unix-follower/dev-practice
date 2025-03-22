from django.http import JsonResponse
from django.http import HttpResponse


def handle_exception(exc, context):
    response_body = {"errorMessage": str(exc)}

    http_response = HttpResponse()
    http_response.status_code = 500
    http_response.headers["Content-Type"] = "application/json"
    http_response.content = JsonResponse(response_body).content
    return http_response
