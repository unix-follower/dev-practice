from typing import Any
from abc import ABC, abstractmethod
from collections.abc import Callable, Awaitable


class AbstractObjectFactory(ABC):
    _creators: dict = {}
    _singletons: dict = {}

    @abstractmethod
    def register_factory_method(
        self, name: str, scope: str, creator: Callable[[...], Any]
    ):
        pass

    @abstractmethod
    def register_async_factory_method(
        self, name: str, scope: str, creator: Callable[[...], Awaitable[Any]]
    ):
        pass

    @abstractmethod
    def get_object(self, name: str):
        pass


class ObjectFactory(AbstractObjectFactory):
    def register_factory_method(
        self, name: str, scope: str, creator: Callable[[...], Any]
    ):
        self._creators[name] = (scope, creator)

    def register_async_factory_method(
        self, name: str, scope: str, creator: Callable[[...], Awaitable[Any]]
    ):
        self._creators[name] = (scope, creator)

    def get_object(self, name: str):
        scope, creator = self._creators[name]
        if scope == "s":
            managed_object = self._singletons.get(name)
            if managed_object is None:
                managed_object = creator()
                self._singletons[name] = managed_object
            return managed_object

        return creator()
