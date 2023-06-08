package eu.pb4.holograms.api.elements;

/**
 * @deprecated Use <a href="https://github.com/Patbox/polymer">Polymer</a>'s virtual entities instead.
 */
@Deprecated
public class SpacingHologramElement extends EmptyHologramElement {
    private double height;

    public SpacingHologramElement(double height) {
        this.height = height;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
