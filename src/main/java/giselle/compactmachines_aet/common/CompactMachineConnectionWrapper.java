package giselle.compactmachines_aet.common;

import appeng.api.networking.IGridConnection;
import appeng.me.service.helpers.ConnectionWrapper;
import dev.compactmods.machines.tunnel.TunnelWallEntity;

public class CompactMachineConnectionWrapper extends ConnectionWrapper
{
	private final TunnelWallEntity tunnelWall;

	public CompactMachineConnectionWrapper(IGridConnection gc, TunnelWallEntity tunnelWall)
	{
		super(gc);

		this.tunnelWall = tunnelWall;
	}

	public TunnelWallEntity getTunnelWall()
	{
		return this.tunnelWall;
	}

}
