package it.polimi.ingsw.Model.Enumerations;

public enum StateGame {

    GAME_ROOM,
    GAME_STARTED,
    GAME_ENDED,
    ROUND_STARTED,
    ROUND_ENDED,
    TURN_STARTED,
    TURN_ENDED,

    /* planning */

    BAG_CLOUDS, //moving student by bag to cloud
    CHOICE_CARD, //choosing assistant card -->3 consequence
    /*action phase */
    PICK_STUDENT, // entry to hall
    MOVE_MOTHER,
    CHOICE_CLOUD,

    CONQUERED_ISLAND,
    UNION_ISLANDS,
    EFFECT_USED,

}
