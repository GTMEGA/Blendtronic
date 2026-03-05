package mega.blendtronic.modules.evilbed;

public final class EvilBedState {
    private static boolean isFused = false;

    private EvilBedState() {
        throw new UnsupportedOperationException();
    }

    public static void setFuse() {
        isFused = true;
    }

    public static boolean getFuse() {
        return isFused;
    }

    public static void resetFuse() {
        isFused = false;
    }
}
