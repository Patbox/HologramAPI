package eu.pb4.holograms.api.elements.item;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import net.minecraft.item.ItemStack;

/**
 * @deprecated Use <a href="https://github.com/Patbox/polymer">Polymer</a>'s virtual entities instead.
 */
@Deprecated
public abstract class AbstractItemHologramElement extends AbstractHologramElement {
    protected ItemStack itemStack;
    protected boolean isDirty = false;

    protected AbstractItemHologramElement(ItemStack stack) {
        super();
        this.itemStack = stack.copy();
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack stack) {
        this.itemStack = stack;
        this.isDirty = true;
    }

    public static AbstractItemHologramElement create(ItemStack stack, boolean isStatic) {
        return isStatic ? new StaticItemHologramElement(stack) : new SpinningItemHologramElement(stack);
    }
}
