from sqlalchemy.orm import DeclarativeBase, MappedAsDataclass


class AbstractDbModel(DeclarativeBase, MappedAsDataclass):
    pass
