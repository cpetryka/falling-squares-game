package petryka.cezary.fallingsquaresgame.enums;

public enum SquareSpeed {
    SLOW(2),
    STANDARD(3),
    FAST(4);

    private final int speed;

    SquareSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
