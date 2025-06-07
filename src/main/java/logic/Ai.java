package logic;
import logic.enums.*;
import lombok.*;
import java.util.*;
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
    private Difficulty difficulty;
    private Symbol simbolo;
    private int symbolIndex = 0;

    //returns the index of the Casella to remove
    public int azione(List<Casella> caselle){
        //Per ogni casella che è vuota (e per sicurezza si controlla anche che la variabile Usata sia false):
        // aggiungo a una List di Integer l'indice della casella vuota così che l'Ai può lavorare con gli indici
        List<Integer> caselleVuote = new ArrayList<>();
        for(var casella : caselle){
            if(casella.getSimbolo() == Symbol.EMPTY && !casella.isUsata())
                caselleVuote.add(caselle.indexOf(casella));
        }
        //in base alla difficoltà dell Ai risponde in modo adeguato
        return switch (difficulty){
            case EASY -> rispostaSemplice(caselleVuote);
            case MEDIUM -> rispostaMedia(caselleVuote);
            case HARD -> rispostaDifficile(caselleVuote);
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
        if(caselle.isEmpty())
            return 0;
        int randomIndex = (int) (Math.random() * caselle.size());
        return caselle.get(randomIndex);
    }
    public static int rispostaMedia(List<Integer> caselle){
        System.out.println("MEDIA");
        return 0;
    }
    public static int rispostaDifficile(List<Integer> caselle){
        System.out.println("DIFFICILE");
        return 0;
    }
}