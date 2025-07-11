package logic;

import static logic.enums.ReturnTurno.*;
import static logic.enums.Symbol.*;

import logic.enums.CheckType;
import logic.enums.ReturnTurno;
import logic.enums.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing game-specific functions in a grid-based game, such as
 * validating the game state, handling player and AI actions, and maintaining the
 * condition of game cells.
 */
public class GameFunctions {
    public static List<Integer> cronologiaAi = new ArrayList<>();


    public static ReturnTurno isOver(Symbol player, List<Casella> caselle, CheckType checkType) {
        char[][] giocate = convertCaselleToChar(caselle);
        char simbolo = player.toString().charAt(0);
        return CheckTable.check(checkType, new char[]{simbolo, simbolo == 'X' ? 'O' : 'X'}, giocate);
    }

    public static char[][] convertCaselleToChar(List<Casella> caselle) {
        // Calcola la dimensione della griglia (radice quadrata della dimensione totale delle caselle)
        int size = (int) Math.sqrt(caselle.size());

        // Verifica che la dimensione sia valida (cioè la lista è una griglia quadrata)
        if (size * size != caselle.size()) {
            throw new IllegalArgumentException("La lista di caselle non rappresenta una griglia quadrata.");
        }

        // Inizializza la matrice
        char[][] giocate = new char[size][size];

        // Converte la lista nella matrice
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                giocate[i][j] = caselle.get(i * size + j).getSimbolo().toString().charAt(0);
            }
        }
        return giocate;
    }

    /**
     * <b>For every {@link Casella} use the function reset</b>
     * @param caselle {@link List} of {@link Casella} to clean
     */
    public static void clean(List<Casella> caselle) {
        for (Casella casella : caselle)
            casella.reset();
    }

//    public static ReturnTurno isFull(List<Casella> caselle){
//        for(var casella : caselle){
//            if(casella.getSimbolo() == Symbol.EMPTY)
//                return NOT_FINISHED;
//        }
//        return TIE;
//    }

    public static ReturnTurno turnoAi(Player player, Ai ai, List<Casella> caselle, CheckType checkType) {
        // Configurazione IA corretta in base al Player
        var sP1 = player.getSimbolo();
        ai.setSimbolo(sP1 == X ? O : X);

        // La lista delle celle deve essere valida
        if (caselle == null || caselle.isEmpty())
            throw new IllegalArgumentException("La lista delle caselle non è valida.");

        if(getAvailablePositions(caselle).isEmpty())
            return TIE;
        System.out.println(caselle.size());

        // L'IA effettua la mossa restituendo l'indice della matrice da selezionare
        var azione = ai.azione(caselle, checkType); // Metodo "azione" decide dove giocare
        cronologiaAi.add(azione);
        // La casella selezionata dall'IA viene aggiornata
        System.out.println(azione);
        caselle.get(azione).seleziona(ai.getSimbolo(), ai.getSymbolIndex());

        // Controlla lo stato del gioco dopo la mossa dell'IA
        return GameFunctions.isOver(player.getSimbolo(), caselle, checkType);
    }
    public static List<Integer> getAvailablePositions(List<Casella> caselle){
        List<Integer> caselleVuote = new ArrayList<>();
        for(var casella : caselle){
            if(casella.getSimbolo() == Symbol.EMPTY && !casella.isUsed())
                caselleVuote.add(caselle.indexOf(casella));
        }
        return caselleVuote;
    }
}