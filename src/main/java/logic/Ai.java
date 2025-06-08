package logic;
import logic.enums.*;
import lombok.*;
import java.util.*;

import static logic.enums.ReturnTurno.*;
import static logic.enums.Symbol.*;

/**
 * Represents an Artificial Intelligence (AI) player in a game.
 * The Ai class simulates gameplay actions based on a specified difficulty level
 * and a given symbol. It uses its difficulty to determine its responses when performing
 * actions on a list of game cells represented by {@link Casella}.
 * <br>
 * <br>
 * <b>This class provides</b>:
 * - Setting and accessing the AI's difficulty and symbol.
 * - Performing AI actions on a given game state by determining the move.
 * - Methods for different difficulty levels to decide on moves.
 * <br>
 * <br>
 * <b>The AI decisions are made based on the difficulty</b>:
 * - EASY: Makes random moves from available positions.
 * - MEDIUM: Placeholder for custom logic for medium difficulty.
 * - HARD: Placeholder for custom logic for hard difficulty.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ai {
    private Difficulty difficulty = Difficulty.EASY;
    private Symbol simbolo;
    private int symbolIndex = 0;

    //returns the index of the Casella to remove
    public int azione(List<Casella> caselle, CheckType checkType){
        //Per ogni casella che è vuota (e per sicurezza si controlla anche che la variabile Usata sia false):
        // aggiungo a una List di Integer l'indice della casella vuota così che l'Ai può lavorare con gli indici
        var caselleVuote = GameFunctions.getAvailablePositions(caselle);

        //in base alla difficoltà dell Ai risponde in modo adeguato
        return switch (difficulty){
            case EASY -> rispostaSemplice(caselleVuote);
            case MEDIUM -> rispostaMedia(caselleVuote, caselle, checkType);
            case HARD -> rispostaDifficile(caselleVuote, caselle, checkType);
            case EXTREME -> rispostaExtrema();
        };
    }

    /**
     * Selects a random element from a list of integers and returns it.
     * If the list is empty, it returns 0.
     *
     * @param caselle a list of integers representing indexes or positions
     *                from which a random selection will be made
     * @return an integer representing a randomly selected element from the list,
     *         or 0 if the list is empty
     */
    public static int rispostaSemplice(List<Integer> caselle){
        int randomIndex = (int) (Math.random() * caselle.size());
        return caselle.get(randomIndex);
    }

    /**
     * Calculates the optimal move based on medium difficulty logic.
     * It first checks if there is a potential winning move for the player,
     * and if so, returns the index of that move. If no winning move is found,
     * it defaults to selecting a random move.
     *
     * @param caselleVuote a list of integers representing the indices of empty cells
     *                     in the game board
     * @param caselleVere  a list of {@link Casella} objects representing the cells
     *                     on the game board
     * @param checkType    a {@link CheckType} enum specifying the type of check
     *                     to perform for a potential winning move
     * @return the index of the selected cell for the move. If a winning move is
     *         found, its index is returned. Otherwise, it falls back to a randomly
     *         selected empty cell.
     */
    public int rispostaMedia(List<Integer> caselleVuote, List<Casella> caselleVere, CheckType checkType) {
        //controllo se e a quale indice può vincere
        int win = checkForWin(caselleVuote, caselleVere, checkType);
        // Se non trova una mossa vincente fa una risposta semplice
        if (win != -1)
            return win;
        return rispostaSemplice(caselleVuote);
    }

    /**
     * Determines the best possible move for the AI based on a combination of offensive
     * and defensive strategies. The method first checks if the AI can make a winning move,
     * followed by checking if it can block the opponent's winning move. If neither condition
     * is met, it resorts to a simple random move strategy.
     *
     * @param caselleVuote a list of integers representing the indices of available, empty positions
     *                     on the game board.
     * @param caselleVere  a list of {@link Casella} objects representing the current state of the
     *                     game board.
     * @param checkType    the type of check to perform for win conditions, represented by {@link CheckType}.
     * @return the index of the selected move. If the AI identifies a winning move, that index is returned.
     *         If a defensive move is identified, its index is returned. Otherwise, an index is randomly
     *         selected from the available positions.
     */
    public int rispostaDifficile(List<Integer> caselleVuote, List<Casella> caselleVere, CheckType checkType){
        //prima controlla se può vincere lui
        var winCheck = checkForWin(caselleVuote, caselleVere, checkType);
        if (winCheck != -1)
            return winCheck;
        //poi controlla se può fermare il player ma solo se prima non può vincere
        var winPlayerCheck = checkForPlayerWin(caselleVuote, caselleVere, checkType);
        if (winPlayerCheck != -1)
            return winPlayerCheck;
        return rispostaSemplice(caselleVuote);
    }

    /**
     * Determines the index of the optimal element to select or remove from the provided list of integers.
     * This method uses advanced logic to evaluate the list and return the most strategic index
     * based on the current game state.
     * @return an integer representing the selected index from the list based on the extreme strategy
     */
    public int rispostaExtrema(){



        return 0;
    }

    /**
     * Simulates and checks possible moves to determine if there is a winning move
     * for the current player. Iterates through the available cells, simulates a move,
     * and checks if it results in a win based on the provided check type.
     * Returns the index of the winning move if found or -1 if no winning move exists.
     *
     * @param caselleVuote a list of integers representing the indexes of currently empty cells.
     * @param caselleVere a list of Casella objects representing the current state of the game board.
     * @param checkType the type of pattern to check for a winning condition (horizontal, diagonal, etc.).
     * @return the index of the winning move if a winning condition is met, or -1 if no winning move is possible.
     */
    public int checkForWin(List<Integer> caselleVuote, List<Casella> caselleVere, CheckType checkType){
        List<Casella> caselleFalse = new ArrayList<>();
        for (Casella casella : caselleVere) {
            caselleFalse.add(new Casella(casella)); // Usa il costruttore di copia
        }

        char simbolo = this.simbolo.toString().charAt(0); // Ottieni il simbolo attuale dell'AI
        //per ogni indice nelle caselle vuote
        for (Integer casella : caselleVuote) {
            // Esegui la simulazione sulla copia (caselleFalse)
            caselleFalse.get(casella).seleziona(this.getSimbolo(), this.getSymbolIndex());

            // Converti la lista simulata in un array di char[][] per il controllo
            char[][] caselleChar = GameFunctions.convertCaselleToChar(caselleFalse);

            // Controlla se è possibile vincere con questa simulazione
            if (CheckTable.check(checkType, new char[]{ simbolo == 'X' ? 'O' : 'X', simbolo}, caselleChar) == P2) {
                return casella; // Restituisci l'indice della mossa vincente
            }
            caselleFalse.get(casella).reset();
        }
        // - 1 per indicare nessuna possibilità di vittoria
        return -1;
    }

    /**
     * Checks if there is a potential winning move for the player based on the current game state.
     * Simulates each possible move and evaluates if it leads to a winning configuration for the opposing player.
     *
     * @param caselleVuote a list of integers representing the indices of empty cells on the game board
     * @param caselleVere a list of Casella objects representing the current state of the game board
     * @param checkType an enum indicating the specific type of check to perform (e.g., horizontal, vertical, diagonal)
     * @return the index of the cell that would allow the player to win, or -1 if no such move exists
     */
    public int checkForPlayerWin(List<Integer> caselleVuote, List<Casella> caselleVere, CheckType checkType){
        List<Casella> caselleFalse = new ArrayList<>();
        for (Casella casella : caselleVere) {
            caselleFalse.add(new Casella(casella)); // Usa il costruttore di copia
        }

        char simbolo = this.simbolo.toString().charAt(0); // Ottieni il simbolo attuale dell'AI
        //per ogni indice nelle caselle vuote
        for (Integer casella : caselleVuote) {
            // Esegui la simulazione sulla copia (caselleFalse)
            caselleFalse.get(casella).seleziona(this.getSimbolo() == X ? O:X, this.getSymbolIndex());

            // Converti la lista simulata in un array di char[][] per il controllo
            char[][] caselleChar = GameFunctions.convertCaselleToChar(caselleFalse);

            // Controlla se è possibile vincere con questa simulazione
            if (CheckTable.check(checkType, new char[]{simbolo == 'X' ? 'O' : 'X', simbolo}, caselleChar) == P1) {
                return casella; // Restituisci l'indice della mossa vincente
            }
            caselleFalse.get(casella).reset();
        }
        // - 1 per indicare nessuna possibilità di vittoria
        return -1;
    }
}