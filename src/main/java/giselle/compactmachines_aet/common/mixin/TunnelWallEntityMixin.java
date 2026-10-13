package giselle.compactmachines_aet.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.location.LevelBlockPosition;
import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import dev.compactmods.machines.tunnel.TunnelItem;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_aet.CompactMachines_AET;
import giselle.compactmachines_aet.common.CompactMachineExtension;
import giselle.compactmachines_aet.common.TunnelWallConnectionWrapper;
import giselle.compactmachines_aet.common.TunnelWallExtension;
import giselle.compactmachines_aet.common.config.ModServerConfig;
import giselle.compactmachines_aet.common.tunnel.definitions.NetworkTunnelDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = TunnelWallEntity.class, remap = false)
public abstract class TunnelWallEntityMixin extends BlockEntity implements TunnelWallExtension
{
	@Shadow(remap = false)
	private LevelBlockPosition connectedMachine;
	@Shadow(remap = false)
	private TunnelDefinition tunnelType;

	private final TunnelWallEntity compactmachines_aet$self = (TunnelWallEntity) (Object) this;
	private TunnelWallConnectionWrapper compactmachines_aet$connection;

	public TunnelWallEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_)
	{
		super(p_155228_, p_155229_, p_155230_);
	}

	@Override
	public ItemStack compactmachines_aet$getItemFromBlockEntity()
	{
		return TunnelItem.createStack(this.tunnelType);
	}

	@Override
	public int compactmachines_aet$getIdlePowerUsage()
	{
		return this.compactmachines_aet$isDenseNode() ? ModServerConfig.DENSE_NETWORK_TUNNEL_USAGE.get() : ModServerConfig.NETWORK_TUNNEL_USAGE.get();
	}

	@Override
	public boolean compactmachines_aet$isInWorldNode()
	{
		return this.tunnelType instanceof NetworkTunnelDefinition;
	}

	@Override
	public boolean compactmachines_aet$isDenseNode()
	{
		return this.tunnelType instanceof NetworkTunnelDefinition definition && definition.isDense();
	}

	@Override
	public void compactmachines_aet$disconnectSelf()
	{
		CompactMachines_AET.disconnect(this.compactmachines_aet$self, null);
	}

	@Override
	public void compactmachines_aet$connectSelf()
	{
		if (this.level instanceof ServerLevel compactLevel)
		{
			if (this.connectedMachine == null)
			{
				return;
			}

			ServerLevel machineLevel = compactLevel.getServer().getLevel(this.connectedMachine.dimensionKey());

			if (machineLevel != null && machineLevel.getBlockEntity(this.connectedMachine.getBlockPosition()) instanceof CompactMachineBlockEntity compactMachine)
			{
				if (((CompactMachineExtension) compactMachine).compactmachines_aet$isInWorldNode())
				{
					CompactMachines_AET.connect(compactMachine, this.compactmachines_aet$self);
				}

			}

		}

	}

	@Override
	public TunnelWallConnectionWrapper compactmachines_aet$getConnection()
	{
		return this.compactmachines_aet$connection;
	}

	@Override
	public void compactmachines_aet$setConnection(TunnelWallConnectionWrapper connection)
	{
		this.compactmachines_aet$connection = connection;
	}

}
