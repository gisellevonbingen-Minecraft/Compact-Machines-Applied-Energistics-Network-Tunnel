package giselle.compactmachines_aet.common.tunnel.definitions;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;

public class NetworkTunnelDefinition implements TunnelDefinition
{
	private final boolean dense;

	public NetworkTunnelDefinition(boolean dense)
	{
		this.dense = dense;
	}

	public boolean isDense()
	{
		return this.dense;
	}

	@Override
	public int ringColor()
	{
		return 0xFFD3B8E8;
	}

}
