package com.bath.teambuilder.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bath.teambuilder.R
import com.bath.teambuilder.ui.theme.TeambuilderTheme


private data class Venue (
    val name: String,
    val distance: Double,
    val logo: Int,
    val website : String,
    val opening_times : String,
    val facilities : List<Facility>
)

private data class Facility (val name : String, val amount : Int, val icon : String)

private val HARDCODED_VENUES = listOf(
    Venue("Sports Training Village", 0.3, R.drawable._teambath,
        "https://www.teambath.com/facilities/", "6am to 10pm",
        listOf(
            Facility("Tennis Court", 2, "🎾"),
            Facility("Football Field", 1, "⚽"),
            Facility("Vollebay Court", 3, "🏐"),
        )
    ),
    Venue("Bath Golf Club", 0.4, R.drawable.bath_golf,
        "https://www.bathgolfclub.org.uk/the_golf_club", "9am to 5pm",
        listOf(
            Facility("Golf", 1, "⛳"),
        )),
    Venue("Bath Cricket Club", 1.1, R.drawable.cricket_club,
        "https://bathcc.play-cricket.com/Aboutus", "9am to 5pm",
        listOf(
            Facility("Cricket", 1, "🏏"),
        )),
    Venue("Staples Center", 5400.0, R.drawable.staples_centre,
        "https://www.cryptoarena.com/events", "9am to 5pm",
        listOf(
            Facility("Boxing", 2, "🥊"),
            Facility("Basketball", 2, "🏀"),
            Facility("Hockey Ring", 1, "🏑"),
        )),
)

@Composable
fun ExploreScreenBody(onNavigation : (String) -> Unit = {}) {

    Column {
        Text(
            text = "Nearby Venues",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(5.dp)
        )
        Divider(color = Color.Gray, thickness = .7.dp)
        Spacer(modifier = Modifier.height(8.dp))
        ShowVenues(HARDCODED_VENUES, onClickScenario = onNavigation)
    }
}

@Composable
private fun ShowVenues(venues: List<Venue>, onClickScenario : (String) -> Unit) {
    LazyColumn (modifier = Modifier.padding(8.dp)){
        items(venues) { scenario -> ScenarioCard(scenario, onClickScenario)}
    }
}

@Composable
private fun ScenarioCard(venue: Venue, onClickScenario: (String) -> Unit) {
    var expanded by remember {mutableStateOf<Boolean>(false)}
    val uriHandler = LocalUriHandler.current
    Row (
        modifier= Modifier.padding(vertical = 8.dp)
    ){
        Row (
            modifier = Modifier
                .weight(1F)
                .clickable {
                    expanded = !expanded
                }
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Image(
                painterResource(venue.logo),
                contentDescription = "Image showing the Caller",
                modifier = Modifier
                    .padding(6.dp)
                    //Set image size to 40dp
                    .size(40.dp)

            )
            Column(
                modifier = Modifier
                    .padding(5.dp)
            )
            {
                Text(
                    text = venue.name,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primaryVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${venue.distance}m away",
                    style = MaterialTheme.typography.caption,
                )

                if (expanded) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Column (verticalArrangement = Arrangement.spacedBy(4.dp)){
                        Row {
                            OutlinedButton(onClick = { uriHandler.openUri(venue.website) }, modifier = Modifier.weight(1F)) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Filled.Web,
                                        contentDescription = "venue link icon"
                                    )
                                    Text("Website", modifier = Modifier.padding(horizontal = 3.dp))
                                }
                            }
                            Row (modifier = Modifier.padding(8.dp)){
                                Icon(imageVector = Icons.Filled.Timer, contentDescription = null)
                                Text(
                                    text = venue.opening_times,
                                    modifier = Modifier.padding(horizontal = 3.dp)
                                )
                            }
                        }
                        Text(text="Facilities")
                        Divider(color= Color.Gray)
                        Column {
                            for (f in venue.facilities)  {
                                Row {
                                    Text(text=f.name, modifier = Modifier.weight(2f))
                                    for (i in 1..f.amount)
                                        Text(text=f.icon, modifier = Modifier.padding(horizontal = 2.dp))
                                }
                            }
                        }

                    }
                }
            }
        }

        IconButton(
            onClick = {expanded = !expanded},
            modifier = Modifier.padding(1.dp)
        ) {
            Icon (
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expanded) "Show less" else "Show more"
            )
        }

    }
}

/*
@Preview(showBackground = true, widthDp=320)
@Composable
fun previewScenarioCard() {
    TeambuilderTheme {

        ScenarioCard(venue = Venue("Sports Training Village", 0.6, R.drawable._teambath), onClickScenario = {})
    }
}


@Preview(showBackground = true, widthDp=320)
@Composable
fun previewFakeCallScreen() {
    TeambuilderTheme {
        ExploreScreenBody(onNavigation = {})
    }
}
 */