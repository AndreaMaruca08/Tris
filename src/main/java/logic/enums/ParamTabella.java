package logic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ParamTabella {
    ANGOLO_UP_RIGHT(2),
    ANGOLO_DOWN_RIGHT(8),
    ANGOLO_DOWN_LEFT(6),
    ANGOLO_UP_LEFT(0),
    FIRST_ROW(0),
    FIRST_COLUMN(0),
    LAST_ROW(2),
    LAST_COLUMN(2);
    @Getter
    private final int value;
}
