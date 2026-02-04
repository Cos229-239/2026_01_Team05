package com.example.mywarriors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check // complete
import androidx.compose.material.icons.filled.Favorite // Health
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star // Mana
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var currentScreen by remember { mutableStateOf("login") }

            when (currentScreen) {
                "login" -> LoginScreen(onLoginSuccess = { currentScreen = "home" })
                "home" -> MainTabScreen(onLogout = { currentScreen = "login" })
            }
        }
    }
}

@Composable
fun MainTabScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                // 1. Home / Quests
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Quests") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WarriorGreen,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Place, contentDescription = "Map") },
                    label = { Text("Map") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WarriorGreen,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Shop") },
                    label = { Text("Shop") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WarriorGreen,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WarriorGreen,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(DarkBackground)) {
            when (selectedTab) {
                0 -> QuestScreen()
                1 -> MapScreen()
                2 -> ShopScreen()
                3 -> ProfileScreen(onLogout)
            }
        }
    }
}

// Login Screen
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("WARRIORS LOGIN", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
            Text("Power up your Warrior with real\nworld workouts and battle the\nevil creatures", fontSize = 16.sp, color = Color.LightGray, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 40.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Username", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = WarriorGreen, unfocusedBorderColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = WarriorGreen, unfocusedBorderColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { onLoginSuccess() },
                colors = ButtonDefaults.buttonColors(containerColor = WarriorGreen),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("LOGIN", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}


//  QUEST SCREEN
@Composable
fun QuestScreen() {
    val db = FirebaseFirestore.getInstance()

    var pushupsEnergy by remember { mutableStateOf(0) }
    var squatsEnergy by remember { mutableStateOf(0) }
    var runEnergy by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.quest_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("GUILD QUESTS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 20.dp))
            Text("Select your bounty", fontSize = 18.sp, color = Color.LightGray, modifier = Modifier.padding(bottom = 30.dp))

            // BUTTON 1 Pushups Earns 20-100 Energy
            QuestButton("100 Pushups", WarriorGreen, pushupsEnergy) {
                if (pushupsEnergy == 0) {
                    val loot = (20..100).random() // Generates Random Loot
                    pushupsEnergy = loot
                    saveQuest(db, "Pushups", loot)
                }
            }

            // BUTTON 2 Squats Earns 20-100 Energy
            QuestButton("100 Squats", WarriorBlue, squatsEnergy) {
                if (squatsEnergy == 0) {
                    val loot = (20..100).random()
                    squatsEnergy = loot
                    saveQuest(db, "Squats", loot)
                }
            }

            // BUTTON 3 Run earns 50-200 Energy
            QuestButton("10 Mile Run", WarriorOrange, runEnergy) {
                if (runEnergy == 0) {
                    val loot = (50..200).random()
                    runEnergy = loot
                    saveQuest(db, "Run", loot)
                }
            }
        }
    }
}


@Composable
fun MapScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg), // Using Login BG for now
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.Place, contentDescription = null, tint = WarriorGreen, modifier = Modifier.size(100.dp))
            Text("The World Map", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Battle Monsters Here", fontSize = 16.sp, color = Color.LightGray)
        }
    }// map
}


@Composable
fun ShopScreen() {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.shop_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)))

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("WYRM'S FORGE", fontSize = 28.sp, color = WarriorOrange, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            // Item 1: Health Potion
            Card(colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.6f)), modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Health", tint = Color.Red, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Dragon Blood", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Restore 50 HP", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("50 Energy bars", color = WarriorOrange, fontWeight = FontWeight.Bold)
                }
            }//hope

            // Item 2: Mana Potion
            Card(colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.6f)), modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = "Mana", tint = Color.Cyan, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Mithril Essence", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Restore 50 MP", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("75 Energy bars", color = WarriorOrange, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.profile_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = WarriorGreen, modifier = Modifier.size(100.dp))
            Text("Guild Master", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Lvl 8 Warrior", fontSize = 18.sp, color = WarriorBlue)

            Spacer(modifier = Modifier.height(30.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatBox("STR", "18")
                StatBox("AGI", "9")
                StatBox("INT", "8")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("LOGOUT", color = Color.White)
            }
        }
    }// Profile
}


@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}





// buttonImpletion
@Composable
fun QuestButton(title: String, color: Color, earnedAmount: Int, onClick: () -> Unit) {
    // If we have more than 0 the quest is complete
    val isCompleted = earnedAmount > 0

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCompleted) Color.DarkGray.copy(alpha = 0.8f) else color.copy(alpha = 0.7f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (isCompleted) BorderStroke(2.dp, WarriorGreen) else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isCompleted) {
                Icon(Icons.Filled.Check, contentDescription = "Done", tint = WarriorGreen, modifier = Modifier.size(30.dp))
                Spacer(modifier = Modifier.width(10.dp))
                // SHOW THE RANDOM LOOT WE EARNED
                Text("+$earnedAmount ENERGY", fontSize = 18.sp, color = WarriorGreen, fontWeight = FontWeight.Bold)
            } else {
                Text(title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun saveQuest(db: FirebaseFirestore, name: String, energy: Int) {
    val questData = hashMapOf("quest_name" to name, "energy_earned" to energy, "timestamp" to System.currentTimeMillis())
    db.collection("quest_history").add(questData)
}


val WarriorGreen = Color(0xFF4CAF50)
val WarriorBlue = Color(0xFF2196F3)
val WarriorOrange = Color(0xFFFF9800)
val DarkBackground = Color(0xFF121212)