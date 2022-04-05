package it.polimi.ingsw.Model.Enumerations;

public enum GameState {
    GAME_CREATED, // GAME_ROOM equivalent, but we do not have a room riight now
    GAME_ROOM,
    GAME_STARTED,
    GAME_ENDED,
    ROUND_STARTED,

    ROUND_ENDED,
    TURN_STARTED,
    TURN_ENDED,

    /* planning */

    BAG_CLOUDS, //moving student by bag to cloud
    PLANNING_PHASE_CHOICE_CARD, //choosing assistant card -->3 consequence
    /*action phase */
    ACTION_PHASE,
    ACTION_PHASE_MOVE_STUDENTS,
    MOVE_MOTHER,
    CHOICE_CLOUD,

    CONQUERED_ISLAND,
    UNION_ISLANDS,
    EFFECT_USED,

}
