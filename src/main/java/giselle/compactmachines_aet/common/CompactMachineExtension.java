package giselle.compactmachines_aet.common;

import java.util.Map;

import net.minecraft.core.BlockPos;

public interface CompactMachineExtension extends NodeBlockExtension
{
	Map<BlockPos, CompactMachineConnectionWrapper> compactmachines_aet$getConnections();

	CompactMachineConnectionWrapper compactmachines_aet$getConnection(BlockPos pos);

	void compactmachines_aet$setConnection(BlockPos pos, CompactMachineConnectionWrapper connection);

	void compactmachines_aet$addDense(BlockPos pos);

	void compactmachines_aet$removeDense(BlockPos pos);
}
