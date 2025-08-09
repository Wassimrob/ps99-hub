package com.pixelmon.utm.features.tournament

import kotlin.random.Random

/** Single-elimination bracket models and generator. */
data class Participant(
    val id: String,
    val displayName: String,
)

data class Match(
    val roundIndex: Int,
    val matchIndex: Int,
    val participantA: Participant?,
    val participantB: Participant?,
)

data class Bracket(
    val rounds: List<List<Match>>,
)

fun generateRandomBracket(participants: List<Participant>, random: Random = Random.Default): Bracket {
    require(participants.size >= 2) { "At least 2 participants required" }

    // If not power-of-two, add BYEs
    val nextPow2 = 1 shl (32 - Integer.numberOfLeadingZeros(participants.size - 1))
    val numByes = nextPow2 - participants.size

    val shuffled = participants.shuffled(random)
    val initialWithByes = buildList {
        addAll(shuffled.map { it })
        repeat(numByes) {
            add(Participant(id = "bye-$it", displayName = "BYE"))
        }
    }

    val roundOf = mutableListOf<List<Match>>()

    var current = initialWithByes.chunked(2).mapIndexed { index, pair ->
        Match(
            roundIndex = 0,
            matchIndex = index,
            participantA = pair.getOrNull(0),
            participantB = pair.getOrNull(1)
        )
    }
    roundOf.add(current)

    var round = 1
    var size = current.size
    while (size > 1) {
        val nextRound = (0 until size / 2).map { idx ->
            Match(
                roundIndex = round,
                matchIndex = idx,
                participantA = null,
                participantB = null
            )
        }
        roundOf.add(nextRound)
        size /= 2
        round += 1
    }

    return Bracket(rounds = roundOf)
}