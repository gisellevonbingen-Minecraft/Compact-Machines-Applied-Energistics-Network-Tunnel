package giselle.compactmachines_aet.common;

import appeng.api.networking.IGridConnection;
import appeng.me.service.helpers.ConnectionWrapper;
import dev.compactmods.machines.machine.CompactMachineBlockEntity;

public class TunnelWallConnectionWrapper extends ConnectionWrapper
{
	private final CompactMachineBlockEntity compactMachine;

	public TunnelWallConnectionWrapper(IGridConnection gc, CompactMachineBlockEntity compactMachine)
	{
		super(gc);

		this.compactMachine = compactMachine;
	}

	public CompactMachineBlockEntity getCompactMachine()
	{
		return this.compactMachine;
	}

}
