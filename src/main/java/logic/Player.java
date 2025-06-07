package logic;

import logic.enums.Symbol;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Player {
    String nome;
    Symbol simbolo;
    public Player(String nome, Symbol simbolo, int simboloIndex) {
        this.nome = nome;
        this.simbolo = simbolo;
        this.symbolIndex = simboloIndex;
    }
    int vittorie = 0;
    int sconfitte = 0;
    int pareggi = 0;
    int partite = 0;
    int symbolIndex;
}
