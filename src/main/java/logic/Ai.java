package logic;
import logic.enums.*;
import lombok.*;

import java.util.*;

import static logic.GameFunctions.convertCaselleToChar;
import static logic.GameFunctions.cronologiaAi;
import static logic.enums.ParamTabella.*;
import static logic.enums.Difficulty.EXTREME;
import static logic.enums.ReturnTurno.*;
import static logic.enums.Symbol.*;
import static ui.UiApplication.cronologiaPlayer;

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
@NoArgsConstructor
public class Ai {
    private Difficulty difficulty = Difficulty.EASY;
    private Symbol simbolo;
    private int symbolIndex = 0;
    private boolean passive = true;


    public Ai(Difficulty difficulty,Symbol simbolo, int symbolIndex) {
        this.simbolo = simbolo;
        this.symbolIndex = symbolIndex;
        this.difficulty = difficulty;
    }
    private static List<Integer> caselleVuote = new ArrayList<>();
    private static List<Casella> caselleVere = new ArrayList<>();
    private static CheckType checkType = CheckType.ALL;
    public static int angoloCasuale = (int) (Math.random() * 4);
    //returns the index of the Casella to remove
    public int azione(List<Casella> caselle, CheckType checkTypePass){
        //Per ogni casella che è vuota (e per sicurezza si controlla anche che la variabile Usata sia false):
        // aggiungo a una List di Integer l'indice della casella vuota così che l'Ai può lavorare con gli indici
        caselleVuote = GameFunctions.getAvailablePositions(caselle);
        checkType = checkTypePass;
        caselleVere = caselle;
        //attacca per primo
        if(!passive && caselleVuote.isEmpty() && difficulty != EXTREME)
            return rispostaExtrema();

        //in base alla difficoltà dell Ai risponde in modo adeguato
        return switch (difficulty){
            case EASY -> rispostaSemplice();
            case MEDIUM -> rispostaMedia();
            case HARD -> rispostaDifficile();
            case EXTREME -> rispostaExtrema();
        };
    }

    /**
     * Selects a random element from a list of integers and returns it.
     * If the list is empty, it returns 0
     *                from which a random selection will be made
     * @return an integer representing a randomly selected element from the list,
     *         or 0 if the list is empty
     */
    public static int rispostaSemplice(){
        int randomIndex = (int) (Math.random() * caselleVuote.size());
        return caselleVuote.get(randomIndex);
    }

    /**
     * Calculates the optimal move based on medium difficulty logic.
     * It first checks if there is a potential winning move for the player,
     * and if so, returns the index of that move. If no winning move is found,
     * it defaults to selecting a random move.

     * @return the index of the selected cell for the move. If a winning move is
     *         found, its index is returned. Otherwise, it falls back to a randomly
     *         selected empty cell.
     */
    public int rispostaMedia() {
        //controllo se e a quale indice può vincere
        int win = checkForWin();
        // Se non trova una mossa vincente fa una risposta semplice
        if (win != -1)
            return win;
        return rispostaSemplice();
    }

    /**
     * Determines the best possible move for the AI based on a combination of offensive
     * and defensive strategies. The method first checks if the AI can make a winning move,
     * followed by checking if it can block the opponent's winning move. If neither condition
     * is met, it resorts to a simple random move strategy.
     * @return the index of the selected move. If the AI identifies a winning move, that index is returned.
     *         If a defensive move is identified, its index is returned. Otherwise, an index is randomly
     *         selected from the available positions.
     */
    public int rispostaDifficile(){
        //prima controlla se può vincere lui
        var winCheck = checkForWin();
        if (winCheck != -1)
            return winCheck;
        //poi controlla se può fermare il player ma solo se prima non può vincere
        var winPlayerCheck = checkForPlayerWin();
        if (winPlayerCheck != -1)
            return winPlayerCheck;
        return rispostaSemplice();
    }
    /**
     * Determines the index of the optimal element to select or remove from the provided list of integers.
     * This method uses advanced logic to evaluate the list and return the most strategic index
     * based on the current game state.
     * @return an integer representing the selected index from the list based on the extreme strategy
     */
    public int rispostaExtrema(){

        if(passive){
           return difesaAngolo();
        }
        return attaccoAngolo();
    }
    public int difesaAngolo(){
        int vuote = caselleVuote.size();
        int vere = caselleVere.size();
        var centro = caselleVere.get(4);
        boolean hasOppositeCornerMove = (vere - cronologiaPlayer.getFirst() - 1) == cronologiaPlayer.getLast();

        //prima mossa -> se angolo -> centro                                                                     prima mossa
        if(vuote == vere - 1 && !centro.isUsed())
            return 4;
        //seconda mossa -> se rimangono
        if ((vuote == vere - 3) && hasOppositeCornerMove) {
            //se il nemico ha posizionato in basso al centro (posizione che vorresti usare tu)
            if (cronologiaPlayer.getLast() == 7)
                return rispostaDifficile();
            return 7;
        }

        // se nessuna condizione risposta difficile
        return rispostaDifficile();
    }

    public int attaccoAngolo(){
        int vere = caselleVere.size();
        var centro = caselleVere.get(4);
        var vuote = caselleVuote.size();
        ParamTabella angolo = switch (angoloCasuale){
            case 0 -> ANGOLO_UP_LEFT;
            case 1 -> ANGOLO_UP_RIGHT;
            case 2 -> ANGOLO_DOWN_LEFT;
            case 3 -> ANGOLO_DOWN_RIGHT;
            default -> throw new IllegalStateException("Angolo inesistente: " + angoloCasuale);
        };
        int row = switch (angoloCasuale){
            case 0, 1 -> FIRST_ROW.getValue();
            case 2, 3 -> LAST_ROW.getValue();
            default -> throw new IllegalStateException("Angolo inesistente " + angoloCasuale);
        };
        int col = switch (angoloCasuale){
            case 0, 2 -> FIRST_COLUMN.getValue();
            case 1, 3 -> LAST_COLUMN.getValue();
            default -> throw new IllegalStateException("Angolo inesistente " + angoloCasuale);
        };
        cronologiaAi.add(angolo.getValue());
        int angoloOpposto = vere - cronologiaAi.getFirst() - 1;


        //----------PRRIMA MOSSA--------------
        if(vuote == vere)
            return angolo.getValue();
        // ---------SECONDA MOSSA-------------
        //route 1 -> risposta a mossa centrale
        if(caselleVere.get(angoloOpposto).isUsed())
            return rispostaDifficile();
        if(vuote == vere - 2 && centro.isUsed())
            return angoloOpposto;
        //route 2 -> vittoria al 100%
        if(vuote == vere - 2  && isRowOccupied(row)){
            System.out.println("SDIJANDOOJASFOWNEFOWOEFMWEOFMWOEIFMWEOIFMWOIEFMWOEIFM");
            return switch (angolo){
                case ANGOLO_UP_LEFT -> ANGOLO_DOWN_LEFT.getValue();
                case ANGOLO_DOWN_LEFT -> ANGOLO_UP_LEFT.getValue();
                case ANGOLO_UP_RIGHT -> ANGOLO_DOWN_RIGHT.getValue();
                case ANGOLO_DOWN_RIGHT -> ANGOLO_UP_RIGHT.getValue();
                default -> throw new IllegalStateException("Angolo inesistente: " + angolo);
            };
        }
        if(vuote == vere - 2  && isColumnOccupied(col)){
            return switch (angolo){
                case ANGOLO_UP_LEFT -> ANGOLO_UP_RIGHT.getValue();
                case ANGOLO_DOWN_LEFT -> ANGOLO_DOWN_RIGHT.getValue();
                case ANGOLO_UP_RIGHT -> ANGOLO_UP_LEFT.getValue();
                case ANGOLO_DOWN_RIGHT -> ANGOLO_DOWN_LEFT.getValue();
                default -> throw new IllegalStateException("Angolo inesistente: " + angolo);
            };
        }

        // ---------TERZA MOSSA------------- VITTORIA
        if (vuote == vere - 4) {
            System.out.println("DENTRO TERZA MOSSA");

            // Se il centro è occupato e l'angolo opposto non è usato
            if (centro.isUsed() && !caselleVere.get(angoloOpposto).isUsed()) {
                System.out.println("Occupo l'angolo opposto per garantire la vittoria.");
                return angoloOpposto;
            }

            // Strategia basata sull'angolo iniziale per completare la vittoria
            return switch (angolo) {
                case ANGOLO_UP_LEFT -> {
                    if (!caselleVere.get(ANGOLO_DOWN_RIGHT.getValue()).isUsed()) {
                        yield ANGOLO_DOWN_RIGHT.getValue();
                    } else {
                        yield ANGOLO_DOWN_LEFT.getValue();
                    }
                }
                case ANGOLO_UP_RIGHT -> {
                    if (!caselleVere.get(ANGOLO_DOWN_LEFT.getValue()).isUsed()) {
                        yield ANGOLO_DOWN_LEFT.getValue();
                    } else {
                        yield ANGOLO_DOWN_RIGHT.getValue();
                    }
                }
                case ANGOLO_DOWN_LEFT -> {
                    if (!caselleVere.get(ANGOLO_UP_RIGHT.getValue()).isUsed()) {
                        yield ANGOLO_UP_RIGHT.getValue();
                    } else {
                        yield ANGOLO_UP_LEFT.getValue();
                    }
                }
                case ANGOLO_DOWN_RIGHT -> {
                    if (!caselleVere.get(ANGOLO_UP_LEFT.getValue()).isUsed()) {
                        yield ANGOLO_UP_LEFT.getValue();
                    } else {
                        yield ANGOLO_UP_RIGHT.getValue();
                    }
                }
                default -> throw new IllegalStateException("Angolo inesistente: " + angolo);
            };
        }





        return rispostaDifficile();
    }
    public boolean isRowOccupied(int row){

        System.out.println("DENTRO RIGHE OCCUPATE");
        char simboloPlayer = this.simbolo.toString().charAt(0) == 'X' ? 'O' : 'X';
        var caselleChar = convertCaselleToChar(caselleVere);
        for(int i = 0; i < 3; i++){
            System.out.println(caselleChar[row][i] + "PROVARIGHE");
            if(caselleChar[row][i] == simboloPlayer){
                return true;
            }
        }
        return false;
    }
    public boolean isColumnOccupied(int column){
        System.out.println("DENTRO COLONNE OCCUPATE");
        char simboloPlayer = this.simbolo.toString().charAt(0) == 'X' ? 'O' : 'X';
        var caselleChar = convertCaselleToChar(caselleVere);
        for(int i = 0; i < 3; i++){
            System.out.println(caselleChar[i][column] + "PROVACOLONNA");
            if(caselleChar[i][column] == simboloPlayer){
                return true;
            }
        }
        return false;
    }

    /**
     * Simulates and checks possible moves to determine if there is a winning move
     * for the current player. Iterates through the available cells, simulates a move,
     * and checks if it results in a win based on the provided check type.
     * Returns the index of the winning move if found or -1 if no winning move exists.
     *
     * @return the index of the winning move if a winning condition is met, or -1 if no winning move is possible.
     */
    public int checkForWin(){
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
            char[][] caselleChar = convertCaselleToChar(caselleFalse);

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
     * @return the index of the cell that would allow the player to win, or -1 if no such move exists
     */
    public int checkForPlayerWin(){
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
            char[][] caselleChar = convertCaselleToChar(caselleFalse);

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