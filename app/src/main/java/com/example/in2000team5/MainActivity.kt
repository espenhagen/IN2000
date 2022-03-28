package com.example.in2000team5
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext
import com.example.in2000team5.ui.theme.IN2000Team5Theme
import com.google.android.gms.maps.model.LatLng


class MainActivity : ComponentActivity() {
    private val viewModel: BicycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.makeApiRequest(this)

        var bicycleRouteList = mutableListOf<BicycleRoute>()

        viewModel.getRoutes().observe(this) {
            bicycleRouteList = it as MutableList<BicycleRoute>
            setContent {
                IN2000Team5Theme() {
                    Column(){
                        InfoRow(uv = 24, sykkelfore = "godt" ) //hardkodet testverdi.
                        VisAlleRuter(bicycleRouteList)
                    }

                }
            }
        }
    }
}
/*
object SampleData {
    val eksempelRuter = listOf(
        SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75,
            listOf(
                LatLng(59.91, 10.75),
                LatLng(59.905, 10.755),
                LatLng(59.91, 10.755),
                LatLng(59.916, 10.78)
            )

        ),
        SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "rainy",
            "20km",
            "54m",
            "good",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "both",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "cloudy",
            "20km",
            "54m",
            "good",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        ), SykkelRute(
            "Grefsen",
            "Kjelsaas",
            23.2.toFloat(),
            "sunny",
            "20km",
            "54m",
            "bad",
            "Dette er en rundtur hvor første halvdel går på glimrende separat sykkelvei fra Grorud stasjon. Det sykles langs toglinje og noe industriområde, men det er stille og rolig. Her går pendlersyklingen unna.",
            59.91,
            10.75
        )


    )
}
*/

