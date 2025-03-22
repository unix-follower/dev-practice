from rest_framework import serializers


class ElementCalcResultResponseSerializer(serializers.Serializer):
    grams = serializers.FloatField()
    molecules = serializers.FloatField()
    moles = serializers.FloatField()
