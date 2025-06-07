package logic;

import logic.enums.CheckType;
import logic.enums.ReturnTurno;
import static logic.enums.ReturnTurno.*;

/**
 * Class containing methods to check if someone has won in a tic-tac-toe game
 */
public class CheckTable {
    /**
     * Checks if there is a horizontal win
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno horizontal(int giro, char[] symbol, char[][] caselle) {
        /*PER OGNI RIGA
              CONTROLLO IN OGNI CASELLA CHE IL CARATTERE NELLA CASELLA SIA DIVERSO DAL SIMBOLO ATTUALE
                 SE VERO: IMPOSTO WIN A FALSO E ROMPO IL CICLO
                 SE FALSO: CONTINUO E MANTENGO WIN A VERO
        */
        for (char[] row : caselle) {
            boolean win = true;
            for (int col = 0; col < caselle.length; col++) {
                if (row[col] != symbol[giro]) {
                    win = false;
                    break;
                }
            }
            if (win) return giro == 0 ? P1 : P2;

        }
        return NOT_FINISHED;
    }

    /**
     * Checks if there is a vertical win
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno vertical(int giro, char[] symbol, char[][] caselle) {
        // HORIZONTAL ma al contrario quindi colonna poi riga
        for (int j = 0; j < caselle.length; j++) {
            boolean win = true;
            for (char[] row : caselle) {
                if (row[j] != symbol[giro]) {
                    win = false;
                    break;
                }
            }
            if (win) return giro == 0 ? P1 : P2;
        }
        return NOT_FINISHED;
    }

    /**
     * Checks for both horizontal and vertical wins
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno linear(int giro, char[] symbol, char[][] caselle) {
        var result = horizontal(giro, symbol, caselle);
        if (result != NOT_FINISHED)
            return result;
        return vertical(giro, symbol, caselle);
    }

    /**
     * Checks for a diagonal win (top-left to bottom-right)
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno diagonal(int giro, char[] symbol, char[][] caselle) {
        /*
        Utilizza caselle[i][i] perchè:
            0 1 2
          0 0 0 0
          1 0 0 0
          2 0 0 0
          quindi la diagonale è l'indice usato sia su righe che colonne

         */
        boolean win = true;
        for (int i = 0; i < caselle.length; i++) {
            if (caselle[i][i] != symbol[giro]) {
                win = false;
                break;
            }
        }
        if (win) return giro == 0 ? P1 : P2;
        return NOT_FINISHED;
    }

    /**
     * Checks for an anti-diagonal win (top-right to a bottom-left)
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno diagonalOpposite(int giro, char[] symbol, char[][] caselle) {
        boolean win = true;
        for (int i = 0; i < caselle.length; i++) {
            if (caselle[i][caselle.length - 1 - i] != symbol[giro]) {
                win = false;
                break;
            }
        }
        if (win) return giro == 0 ? P1 : P2;
        return NOT_FINISHED;
    }

    /**
     * Checks for both diagonal win conditions
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno diagonali(int giro, char[] symbol, char[][] caselle) {
        var result = diagonal(giro, symbol, caselle);
        if (result != NOT_FINISHED)
            return result;
        return diagonalOpposite(giro, symbol, caselle);
    }
    /**
     * Checks for all possible win conditions
     *
     * @param giro    Current turn (0 for player 1, 1 for player 2)
     * @param symbol  Symbol to check for (X or O)
     * @param caselle 3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno all(int giro, char[] symbol, char[][] caselle) {
        var result = linear(giro, symbol, caselle);
        if (result != NOT_FINISHED)
            return result;
        return diagonali(giro, symbol, caselle);
    }

    /**
     * Main check method that performs win checks based on provided CheckType
     *
     * @param checkType Type of check to perform (HORIZONTAL, VERTICAL, etc.)
     * @param symbol    Symbol to check for (X or O)
     * @param caselle   3x3 game board array
     * @return P1 if player 1 won, P2 if player 2 won, NOT_FINISHED otherwise
     */
    public static ReturnTurno check(CheckType checkType, char[] symbol, char[][] caselle) {
        for (int giro = 0; giro < 2; giro++) {
            var returnValue = switch (checkType) {
                case HORIZONTAL -> horizontal(giro, symbol, caselle);
                case VERTICALE -> vertical(giro, symbol, caselle);
                case LINEAR -> linear(giro, symbol, caselle);
                case DIAGONALE -> diagonal(giro, symbol, caselle);
                case ANTIDIAGONALE -> diagonalOpposite(giro, symbol, caselle);
                case OBLIQUO -> diagonali(giro, symbol, caselle);
                case ALL -> all(giro, symbol, caselle);
            };

            if (returnValue != NOT_FINISHED) {
                return returnValue;
            }
        }
        return NOT_FINISHED;
    }
}