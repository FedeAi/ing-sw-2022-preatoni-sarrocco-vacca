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
    ACTION_MOVE_MOTHER,
    ACTION_CHOOSE_CLOUD,

    /* CharacterCards States     */
    HERALD_ACTIVE,
    MUSHROOM_CHOOSE_COLOR,
    JOKER_SWAP_STUDENTS,
    PRINCESS_MOVE_STUDENT,
    MONK_MOVE_STUDENT,
    GRANDMA_BLOCK_ISLAND,
    MINISTREL_SWAP_STUDENTS,

}
