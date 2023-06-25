package petryka.cezary.fallingsquaresgame.enums;

public enum SquareCreationProbability {
    LOW(0.02),
    STANDARD(0.03),
    HIGH(0.04);

    private final double probability;

    SquareCreationProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
