package giselle.compactmachines_aet.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.networking.GridFlags;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IManagedGridNode;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_aet.CompactMachines_AET;
import giselle.compactmachines_aet.common.NodeBlockExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(value = {CompactMachineBlockEntity.class, TunnelWallEntity.class}, remap = false)
public abstract class NodeBlockEntityMixin extends BlockEntityMixin implements NodeBlockExtension, IGridConnectedBlockEntity
{
	private final BlockEntity compactmachines_aet$self = (BlockEntity) (Object) this;
	private IManagedGridNode compactmachines_aet$mainNode = this.compactmachines_aet$createMainNode();

	private IManagedGridNode compactmachines_aet$createMainNode()
	{
		return GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE).setInWorldNode(true).setTagName("proxy");
	}

	@Override
	public IManagedGridNode getMainNode()
	{
		return this.compactmachines_aet$mainNode;
	}

	@Override
	public void saveChanges()
	{

	}

	private void compactmachines_aet$onReady(BlockEntity self)
	{
		this.compactmachines_aet$create();
	}

	@Override
	protected void compactmachines_aet$onSetRemoved()
	{
		super.compactmachines_aet$onSetRemoved();

		this.compactmachines_aet$destory();
	}

	@Override
	protected void compactmachines_aet$onClearRemoved()
	{
		super.compactmachines_aet$onClearRemoved();

		GridHelper.onFirstTick(this.compactmachines_aet$self, this::compactmachines_aet$onReady);
	}

	@Override
	public void compactmachines_aet$recreate()
	{
		this.compactmachines_aet$destory();
		this.compactmachines_aet$mainNode = this.compactmachines_aet$createMainNode();
		this.compactmachines_aet$create();
	}

	private void compactmachines_aet$create()
	{
		this.compactmachines_aet$mainNode.setInWorldNode(this.compactmachines_aet$isInWorldNode());
		this.compactmachines_aet$mainNode.setVisualRepresentation(this.compactmachines_aet$getItemFromBlockEntity());
		this.compactmachines_aet$mainNode.setIdlePowerUsage(this.compactmachines_aet$getIdlePowerUsage());

		if (this.compactmachines_aet$isDenseNode())
		{
			this.compactmachines_aet$mainNode.setFlags(GridFlags.DENSE_CAPACITY);
		}

		this.getMainNode().create(this.level, this.worldPosition);

		this.compactmachines_aet$connectSelf();
	}

	private void compactmachines_aet$destory()
	{
		this.getMainNode().destroy();
		this.compactmachines_aet$disconnectSelf();
	}

	@Inject(method = "load", remap = true, at = @At(value = "tail"), cancellable = false)
	private void load(CompoundTag tag, CallbackInfo ci)
	{
		this.getMainNode().loadFromNBT(tag.getCompound(CompactMachines_AET.rl("main_node").toString()));
	}

	@Inject(method = "saveAdditional", remap = true, at = @At(value = "tail"), cancellable = false)
	public void saveAdditional(CompoundTag tag, CallbackInfo ci)
	{
		this.getMainNode().saveToNBT(tag.getCompound(CompactMachines_AET.rl("main_node").toString()));
	}

	// @Inject(method = "onLoad", remap = false, at = @At(value = "tail"), cancellable = false)
	// private void onLoad(CallbackInfo ci)
	// {
	// this.extension.compactmachines_aet$connectSelf();
	//
	// GridHelper.onFirstTick(this.self, this::compactmachines_aet$onReady);
	// }

}
