package it.polimi.ingsw.Model.Enumerations;

public enum GameState {
    GAME_ROOM,
    GAME_STARTED,
    GAME_ENDED,
    ROUND_STARTED,

    ROUND_ENDED,
    TURN_STARTED,
    TURN_ENDED,

    /* planning */

    PLANNING_CHOOSE_CARD, //choosing assistant card -->3 consequence
    /* action phase */
    ACTION_MOVE_STUDENTS,
    ACTION_PLAY_CHARACTER,
    ACTION_MOVE_MOTHER,
    ACTION_CHOOSE_CLOUD,

    /* CharacterCards */
    HERALD_ACTIVE,
}
