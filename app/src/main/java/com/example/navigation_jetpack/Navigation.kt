package com.example.navigation_jetpack

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route){

        // Screen 1
        composable(route = Screen.MainScreen.route){
            MainScreen(navController = navController)
        }

        // Screen 2
        composable(
            // pass either "/{name}" [mandatory to give name] OR "?name = {name}"
            route = Screen.DetailScreen.route + "/{name}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                    defaultValue = "Shobhit"
                    nullable = true
                }
            )
        ){ // backStackEntry
            DetailScreen(
                text = it.arguments?.getString("name"),
                navController = navController
            )
        }

        // Screen 3
        composable("pokemon_list_screen"){
            PokemonListScreen(navController = navController)
        }

    }

}

@Composable
fun MainScreen(navController: NavController) {

    var text by remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {
                // onValueChange it gives us the new string whenever the value changes
                // and we just update our text with the new string
                text = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.DetailScreen.withArgs(text))
            },
            modifier = Modifier.align(Alignment.End)
            ) {
            Text(text = "To DetailScreen / 2nd Screen")
        }
        val activity = (LocalContext.current as? Activity)
        Button(onClick = {
//            navController.popBackStack()
            activity?.finish()
        }) {
            Text("Exit")
        }
    }
}


@Composable
fun DetailScreen(text: String?, navController: NavController) {
    Box( contentAlignment = Alignment.Center
        ,modifier = Modifier
            .fillMaxSize()
    ){

        Column {
            Text(text = "Hello $text")

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.MainScreen.route)
                },
                modifier = Modifier
            ) {
                Text(text = "To Main Screen / 1st Screen")
            }

            val activity = (LocalContext.current as? Activity)

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Back")
            }
            Button(onClick = {
//                navController.popBackStack()
                activity?.finish()
            }) {
                Text("Exit")
            }
            Button(
                onClick = {
                    navController.navigate("pokemon_list_screen")
                }
            ) {
                Text(text = "Next")
            }
        }
    }

}


@Composable
fun PokemonListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = androidx.core.R.drawable.ic_call_answer),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Back")
            }
            val activity = (LocalContext.current as? Activity)
            Button(onClick = {
                activity?.finish()
            }) {
                Text("Exit")
            }
        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        // if hint is not equal to empty string
        // so if we pass something for the hint this will start with true
        // and if we pass empty string here then this will be false
        mutableStateOf( hint  != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(vertical = 12.dp, horizontal = 20.dp)
                // it will get called when we clicked into that textfield bcz then we want to hind our hint
                .onFocusChanged {
                    // hint tab show hona jab BTF ka focusState false hona
//                    isHintDisplayed = it != FocusState.Active
                    isHintDisplayed = !it.isFocused
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}