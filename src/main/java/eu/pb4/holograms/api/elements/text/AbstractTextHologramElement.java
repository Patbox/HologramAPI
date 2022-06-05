package eu.pb4.holograms.api.elements.text;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;


public abstract class AbstractTextHologramElement extends AbstractHologramElement {
    protected Text text;
    protected int entityId;
    protected UUID uuid;
    protected boolean isDirty = false;

    public AbstractTextHologramElement() {
        this(Text.empty());
    }

    protected AbstractTextHologramElement(Text text) {
        this.text = text;
        this.height = 0.28;
    }

    public static AbstractTextHologramElement create(Text text, boolean isStatic) {
        return isStatic ? new StaticTextHologramElement(text) : new MovingTextHologramElement(text);
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
        this.isDirty = true;
    }

    public Text getTextFor(ServerPlayerEntity player) {
        return this.getText();
    }
}
