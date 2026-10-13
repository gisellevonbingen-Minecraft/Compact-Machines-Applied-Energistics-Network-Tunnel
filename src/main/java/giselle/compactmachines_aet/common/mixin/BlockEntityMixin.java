package giselle.compactmachines_aet.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(value = BlockEntity.class, remap = true)
public abstract class BlockEntityMixin
{
	@Shadow(remap = true)
	protected Level level;
	@Shadow(remap = true)
	protected BlockPos worldPosition;

	@Inject(method = "setRemoved", remap = true, at = @At(value = "head"), cancellable = false)
	private void setRemoved(CallbackInfo ci)
	{
		this.compactmachines_aet$onSetRemoved();
	}

	@Inject(method = "clearRemoved", remap = true, at = @At(value = "head"), cancellable = false)
	private void clearRemoved(CallbackInfo ci)
	{
		this.compactmachines_aet$onClearRemoved();
	}

	protected void compactmachines_aet$onSetRemoved()
	{

	}

	protected void compactmachines_aet$onClearRemoved()
	{

	}

}
