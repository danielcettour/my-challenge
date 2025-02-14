package com.cettourdev.challenge.screens.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp),
                text = "Acerca de esta aplicación",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("App de búsqueda de artículos, desarrollada como challenge para MercadoLibre.", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(40.dp))
        Text("Para el desarrollo se utilizó:", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(18.dp))
        Text("- JetPack Compose con Material3", fontSize = 14.sp)
        Text("- Clean architecture y MVVM", fontSize = 14.sp)
        Text("- Inyección de dependencias con Dagger/Hilt", fontSize = 14.sp)
        Text("- Retrofit para obtener datos de API", fontSize = 14.sp)
        Text("- Room para el manejo de datos locales", fontSize = 14.sp)
        Text("- Firebase Crashlytics para seguimiento de errores", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Info", modifier = Modifier.size(30.dp), tint = Color.Gray)

            Text("Versión: 1.0.0", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Desarrollada por: Daniel Cettour", fontSize = 16.sp)
            MyGithubRepository()
        }

    }
}

@Composable
fun MyGithubRepository() {
    val annotatedLinkString: AnnotatedString = remember {
        buildAnnotatedString {
            val style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline
            )
            withLink(LinkAnnotation.Url(url = "https://github.com/danielcettour/my-challenge")) {
                withStyle(
                    style = style
                ) {
                    append("github.com/danielcettour/my-challenge")
                }
            }
        }
    }
    Text(annotatedLinkString)
}


