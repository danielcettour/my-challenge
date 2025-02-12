package com.cettourdev.challenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * clase principal que se agrega al manifest para configurar inyección de dependencias
 */

@HiltAndroidApp
class ChallengeApp : Application()
