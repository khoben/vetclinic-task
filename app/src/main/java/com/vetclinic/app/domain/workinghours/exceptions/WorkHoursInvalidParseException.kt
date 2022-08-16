package com.vetclinic.app.domain.workinghours.exceptions

class WorkHoursInvalidParseException(val origin: Exception) :
    RuntimeException("Work hours can't be parsed properly")