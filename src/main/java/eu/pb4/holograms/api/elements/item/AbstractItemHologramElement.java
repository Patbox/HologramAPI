package eu.pb4.holograms.api.elements.item;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import net.minecraft.item.ItemStack;

public abstract class AbstractItemHologramElement extends AbstractHologramElement {
    protected ItemStack itemStack;

    protected AbstractItemHologramElement(ItemStack stack) {
        super();
        this.itemStack = stack.copy();
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack stack) {
        this.itemStack = stack;
    }
}
