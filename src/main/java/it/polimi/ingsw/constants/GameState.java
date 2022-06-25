package it.polimi.ingsw.constants;

/**
 * Class GameState represents all the possibile internal game states.
 */
public enum GameState {
    GAME_ROOM,
    INITIAL_FIRE_COMPLETED,
    // FIXME THESE ARENT USED (?)
    GAME_STARTED,
    GAME_ENDED,
    ROUND_STARTED,

    ROUND_ENDED,
    TURN_STARTED,
    TURN_ENDED,
    /* initial magician selection phase */
    SETUP_CHOOSE_MAGICIAN,

    /* Planning phase */
    PLANNING_CHOOSE_CARD,

    /* Action phase */
    ACTION_MOVE_STUDENTS,
    ACTION_MOVE_MOTHER,
    ACTION_CHOOSE_CLOUD,

    /* Character cards states (accessibile only by activating the homonym character card) */
    HERALD_ACTIVE,
    MUSHROOM_CHOOSE_COLOR,
    JOKER_SWAP_STUDENTS,
    PRINCESS_MOVE_STUDENT,
    MONK_MOVE_STUDENT,
    GRANDMA_BLOCK_ISLAND,
    MINSTREL_SWAP_STUDENTS,
    THIEF_CHOOSE_COLOR,
}