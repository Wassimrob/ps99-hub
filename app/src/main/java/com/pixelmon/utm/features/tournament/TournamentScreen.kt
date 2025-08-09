package com.pixelmon.utm.features.tournament

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun TournamentScreen(paddingValues: PaddingValues) {
    val participants = remember { mutableStateListOf<Participant>() }
    val bracketState = remember { mutableStateListOf<List<Match>>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Random Tournament (Smash MC style)", style = MaterialTheme.typography.titleLarge)

        var newName = remember { androidx.compose.runtime.mutableStateOf("") }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = newName.value,
                onValueChange = { newName.value = it },
                label = { Text("Add participant") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                val trimmed = newName.value.trim()
                if (trimmed.isNotEmpty()) {
                    participants.add(
                        Participant(
                            id = System.currentTimeMillis().toString() + Random.nextInt(),
                            displayName = trimmed
                        )
                    )
                    newName.value = ""
                }
            }) { Text("Add") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (participants.size >= 2) {
                    val bracket = generateRandomBracket(participants)
                    bracketState.clear()
                    bracketState.addAll(bracket.rounds)
                }
            }) { Text("Generate Bracket") }
            Button(onClick = {
                participants.clear()
                bracketState.clear()
            }) { Text("Clear") }
        }

        Text("Participants: ${participants.size}")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(participants) { p ->
                Card { Text(p.displayName, modifier = Modifier.padding(12.dp)) }
            }
        }

        Spacer(Modifier.height(16.dp))
        if (bracketState.isNotEmpty()) {
            Text("Bracket", style = MaterialTheme.typography.titleMedium)
            bracketState.forEachIndexed { roundIndex, matches ->
                Text("Round ${roundIndex + 1}", style = MaterialTheme.typography.titleSmall)
                matches.forEach { m ->
                    Card(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Text(text = (m.participantA?.displayName ?: "TBD") + " vs " + (m.participantB?.displayName ?: "TBD"))
                        }
                    }
                }
            }
        }
    }
}