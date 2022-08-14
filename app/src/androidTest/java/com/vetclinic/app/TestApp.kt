package com.vetclinic.app

import com.vetclinic.app.di.DiContainer
import com.vetclinic.app.testing.mock.MockDiContainer

class TestApp : App() {
    override val appContainer: DiContainer = MockDiContainer
}