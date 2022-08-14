package com.vetclinic.app.common.network.exception

import java.io.IOException

class BadHttpServiceCall(val code: Int, val text: String) : IOException("Bad HTTP service call")