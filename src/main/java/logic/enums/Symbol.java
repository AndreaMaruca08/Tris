package logic.enums;

import lombok.Getter;

public enum Symbol {
    X(new String[]{"/img/X_base.png", "/img/X_type1.png", "/img/X_type2.png"}),
    O(new String[]{"/img/O_base.png","/img/O_type1.png", "/img/O_type2.png"}),
    EMPTY(new String[0]);

    @Getter
    private final String[] paths;

    Symbol(String[] paths) {
        this.paths = paths;
    }

    // Facoltativo: metodo per prendere una path casuale o tramite indice
    public String getPath(int index) {
        if (paths.length == 0) return null;
        if (index < 0 || index >= paths.length) index = 0;
        return paths[index];
    }
    // Facoltativo: path casuale
    public String getRandomPath() {
        if (paths.length == 0) return null;
        int idx = (int) (Math.random() * paths.length);
        return paths[idx];
    }

}
