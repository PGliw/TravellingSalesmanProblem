package main.utils

class DataFileFormatException(message: String) : RuntimeException(message)
class IncompatibleCitiesException(message: String) : RuntimeException(message)
class RouteNotFoundException(message: String) : RuntimeException(message)
class IncompatibleRoutesException(message: String) : RuntimeException(message)
class FitnessProviderNotRegisteredException(message: String) : RuntimeException(message)
class InvalidSelctorException(message: String) : RuntimeException(message)