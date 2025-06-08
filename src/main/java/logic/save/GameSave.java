package logic.save;

import logic.Ai;
import logic.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSave {
    private Player player;
    private Ai ai;
}