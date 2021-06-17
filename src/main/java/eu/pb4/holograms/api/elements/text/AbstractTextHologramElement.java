package eu.pb4.holograms.api.elements.text;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.ArmorStandEntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityTrackerUpdateS2CPacketAccessor;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public abstract class AbstractTextHologramElement extends AbstractHologramElement {
    protected Text text;
    protected int entityId;
    protected UUID uuid;
    protected boolean isDirty = false;

    public AbstractTextHologramElement() {
        this(new LiteralText(""));
    }

    protected AbstractTextHologramElement(Text text) {
        this.text = text;
        this.height = 0.28;
    }

    public Text getText() {
        return this.text;
    }

    public Text getTextFor(ServerPlayerEntity player) {
        return this.getText();
    }

    public void setText(Text text) {
        this.text = text;
        this.isDirty = true;
    }

    public static AbstractTextHologramElement create(Text text, boolean isStatic) {
        return isStatic ? new StaticTextHologramElement(text) : new MovingTextHologramElement(text);
    }
}
