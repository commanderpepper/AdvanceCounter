package com.commanderpepper.advancecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.commanderpepper.advancecounter.ui.counterdetails.CounterDetails
import com.commanderpepper.advancecounter.ui.parentcounters.ParentCounters
import com.commanderpepper.advancecounter.ui.theme.AdvanceCounterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvanceCounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvanceCounterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "counter/parents"){
                        composable("counter/parents"){
                            ParentCounters(
                                addCounterImageResource = R.drawable.ic_add,
                                topAppBarTitle = "Parent Counters",
                                counterOptionImageResource = R.drawable.ic_more_options
                            ){ id ->
                                navController.navigate(
                                    "counters/?counterId={counterId}".replace(
                                        oldValue = "{counterId}",
                                        newValue = id.toString()
                                    )
                                )
                            }
                        }
                        composable("counters/?counterId={counterId}"){
                            CounterDetails(
                                addCounterImageResource = R.drawable.ic_add,
                                counterOptionImageResource = R.drawable.ic_more_options
                            ) { id ->
                                navController.navigate(
                                    "counters/?counterId={counterId}".replace(
                                        oldValue = "{counterId}",
                                        newValue = id.toString()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}