package logic;

import logic.enums.Symbol;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ui.UiApplication;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * The Casella class represents a customizable button designed to be used
 * as a cell in a grid-based game, such as Tic-Tac-Toe. It extends the functionality
 * of a {@link JButton} and adds specific properties and behaviors to represent
 * and manage its state.
 * <br>
 * <br>
 * The Casella class includes features for handling a {@link Symbol}, whether
 * the cell has been selected (used), visual feedback through icons, and methods
 * to simulate interactions and reset its state.
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class Casella extends JButton {
    private Symbol simbolo = Symbol.EMPTY;
    private boolean used = false;

    public Casella() {
        super();
    }
    // Aggiungi un costruttore di copia per la deep copy
    public Casella(Casella original) {
        this.simbolo = original.simbolo;
        this.used = original.used;
    }


    //simula il click della casella
    public void seleziona(Symbol simbolo, int symbolIndex) {
        //se già used fa return
        if (this.used)
            return;

        //prende il percorso dell'immagine X O
        String path = simbolo.getPath(symbolIndex);
        if (path == null) {
            log.error("Immagine non disponibile per il simbolo {}", simbolo.name());
            return;
        }

        //imposta l'immagine
        URL url = UiApplication.class.getResource(path);
        if (url != null) {
            // Usa dimensioni predefinite se width o height sono 0
            int width = this.getWidth() > 0 ? this.getWidth() : 100; // Usa 100 come larghezza predefinita
            int height = this.getHeight() > 0 ? this.getHeight() : 100; // Usa 100 come altezza predefinita

            ImageIcon originalIcon = new ImageIcon(url);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        } else {
            log.error("Impossibile trovare l'immagine: {} ", path);
            return;
        }

        //imposta Usata a true e il simbolo siccome è stata selezionata
        this.setUsed(true);
        this.setSimbolo(simbolo);
        this.setFocusPainted(false);
        this.setOpaque(false);

        //aggiorna
        this.revalidate();
        this.repaint();
    }


    public void reset() {
        this.used = false;
        this.setIcon(null);
        this.setSimbolo(Symbol.EMPTY);

        //aggiorna la casella
        this.revalidate();
        this.repaint();
    }

}