package com.vetclinic.app.data.cloud.exceptions

import java.io.IOException

class InvalidPetListDataException(val origin: Exception) : IOException("Invalid pet list data")