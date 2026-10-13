package giselle.compactmachines_aet.common;

import net.minecraft.world.item.ItemStack;

public interface NodeBlockExtension
{
	void compactmachines_aet$recreate();

	ItemStack compactmachines_aet$getItemFromBlockEntity();

	int compactmachines_aet$getIdlePowerUsage();

	boolean compactmachines_aet$isInWorldNode();

	boolean compactmachines_aet$isDenseNode();

	void compactmachines_aet$disconnectSelf();

	void compactmachines_aet$connectSelf();
}
