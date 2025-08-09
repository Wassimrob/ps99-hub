package com.pixelmon.utm.features.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun TeamManagerScreen(paddingValues: PaddingValues) {
    val teams = remember { mutableStateListOf<Team>() }
    val species = remember { SpeciesDataRepository.sampleSpecies }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("UTM: Team Manager", style = MaterialTheme.typography.titleLarge)

        val teamName = remember { androidx.compose.runtime.mutableStateOf("") }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = teamName.value,
                onValueChange = { teamName.value = it },
                label = { Text("New team name") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                val name = teamName.value.trim()
                if (name.isNotEmpty()) {
                    teams.add(Team(name = name))
                    teamName.value = ""
                }
            }) { Text("Create") }
        }

        Text("Teams: ${'$'}{teams.size}")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(teams) { team ->
                Card(modifier = Modifier.padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(team.name, style = MaterialTheme.typography.titleMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            species.take(6).forEach { sp ->
                                Button(onClick = {
                                    val updated = team.members.toMutableList()
                                    if (updated.size < 6) {
                                        updated.add(TeamMember(species = sp))
                                        val idx = teams.indexOf(team)
                                        if (idx >= 0) {
                                            teams[idx] = team.copy(members = updated)
                                        }
                                    }
                                }) { Text(sp.name) }
                            }
                            Button(onClick = {
                                val randomized = species.shuffled().take(6).map { TeamMember(it) }
                                val idx = teams.indexOf(team)
                                if (idx >= 0) teams[idx] = team.copy(members = randomized)
                            }) { Text("Random 6") }
                        }
                        if (team.members.isNotEmpty()) {
                            Text("Members:")
                            team.members.forEach { member ->
                                Text("- ${'$'}{member.species.name}")
                            }
                        }
                    }
                }
            }
        }
    }
}