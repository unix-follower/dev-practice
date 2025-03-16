from sqlalchemy.orm import DeclarativeBase, MappedAsDataclass


# pylint: disable=too-few-public-methods
class AbstractDbModel(DeclarativeBase, MappedAsDataclass):
    pass
