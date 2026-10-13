package giselle.compactmachines_aet.common.mixin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import dev.compactmods.machines.tunnel.graph.TunnelConnectionGraph;
import giselle.compactmachines_aet.CompactMachines_AET;
import giselle.compactmachines_aet.common.CompactMachineConnectionWrapper;
import giselle.compactmachines_aet.common.CompactMachineExtension;
import giselle.compactmachines_aet.common.TunnelWallExtension;
import giselle.compactmachines_aet.common.config.ModServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = CompactMachineBlockEntity.class, remap = false)
public abstract class CompactMachineBlockEntityMixin extends BlockEntity implements CompactMachineExtension
{
	@Shadow
	private ChunkPos roomChunk;

	private final CompactMachineBlockEntity compactmachines_aet$self = (CompactMachineBlockEntity) (Object) this;
	private final Map<BlockPos, CompactMachineConnectionWrapper> compactmachines_aet$connections = new HashMap<>();
	private final Set<BlockPos> compactmachines_aet$denses = new HashSet<>();
	private boolean compactmachines_aet$isDenseNode;

	public CompactMachineBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_)
	{
		super(p_155228_, p_155229_, p_155230_);
	}

	@Override
	public ItemStack compactmachines_aet$getItemFromBlockEntity()
	{
		return new ItemStack(this.getBlockState().getBlock());
	}

	@Override
	public int compactmachines_aet$getIdlePowerUsage()
	{
		return ModServerConfig.COMPACT_MACHINE_USAGE.get();
	}

	@Override
	public boolean compactmachines_aet$isInWorldNode()
	{
		return true;
	}

	@Override
	public boolean compactmachines_aet$isDenseNode()
	{
		return this.compactmachines_aet$isDenseNode;
	}

	@Override
	public void compactmachines_aet$removeDense(BlockPos pos)
	{
		if (this.compactmachines_aet$denses.remove(pos))
		{
			this.compactmachines_aet$updateDenseNode();
		}

	}

	@Override
	public void compactmachines_aet$addDense(BlockPos pos)
	{
		if (this.compactmachines_aet$denses.add(pos))
		{
			this.compactmachines_aet$updateDenseNode();
		}

	}

	private void compactmachines_aet$updateDenseNode()
	{
		boolean isDenseNode = !this.compactmachines_aet$denses.isEmpty();

		if (this.compactmachines_aet$isDenseNode != isDenseNode)
		{
			this.compactmachines_aet$isDenseNode = isDenseNode;
			this.compactmachines_aet$recreate();
		}

	}

	@Override
	public void compactmachines_aet$disconnectSelf()
	{
		this.compactmachines_aet$connections.values().forEach(CompactMachines_AET::disconnect);
	}

	@Override
	public void compactmachines_aet$connectSelf()
	{
		if (this.level instanceof ServerLevel machineLevel)
		{
			ServerLevel compactLevel = CompactDimension.forServer(machineLevel.getServer());

			if (compactLevel == null)
			{
				return;
			}

			ChunkPos roomChunk = this.compactmachines_aet$self.getConnectedRoom().orElse(null);

			if (roomChunk == null)
			{
				return;
			}

			TunnelConnectionGraph tunnelGraph = TunnelConnectionGraph.forRoom(compactLevel, roomChunk);
			tunnelGraph.tunnels().forEach(node ->
			{
				if (compactLevel.getBlockEntity(node.location()) instanceof TunnelWallEntity tunnelWall)
				{
					if (((TunnelWallExtension) tunnelWall).compactmachines_aet$isInWorldNode())
					{
						CompactMachines_AET.connect(this.compactmachines_aet$self, tunnelWall);
					}

				}

			});

		}

	}

	@Override
	public Map<BlockPos, CompactMachineConnectionWrapper> compactmachines_aet$getConnections()
	{
		return new HashMap<>(this.compactmachines_aet$connections);
	}

	@Override
	public CompactMachineConnectionWrapper compactmachines_aet$getConnection(BlockPos pos)
	{
		return this.compactmachines_aet$connections.get(pos);
	}

	@Override
	public void compactmachines_aet$setConnection(BlockPos pos, CompactMachineConnectionWrapper connection)
	{
		this.compactmachines_aet$connections.put(pos, connection);
	}

}
