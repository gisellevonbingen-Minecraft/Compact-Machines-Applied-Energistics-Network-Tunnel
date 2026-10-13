package giselle.compactmachines_aet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridNode;
import appeng.me.helpers.IGridConnectedBlockEntity;
import appeng.me.service.helpers.ConnectionWrapper;
import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import dev.compactmods.machines.tunnel.TunnelItem;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_aet.common.CompactMachineConnectionWrapper;
import giselle.compactmachines_aet.common.CompactMachineExtension;
import giselle.compactmachines_aet.common.TunnelWallConnectionWrapper;
import giselle.compactmachines_aet.common.TunnelWallExtension;
import giselle.compactmachines_aet.common.config.ModServerConfig;
import giselle.compactmachines_aet.common.init.ModTunnels;
import giselle.compactmachines_aet.common.tunnel.definitions.NetworkTunnelDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CompactMachines_AET.MODID)
public class CompactMachines_AET
{
	public static final String MODID = "compactmachines_aet";
	public static final Logger LOGGER = LogManager.getLogger();

	public CompactMachines_AET()
	{
		ModLoadingContext fml_context = ModLoadingContext.get();
		fml_context.registerConfig(ModConfig.Type.SERVER, ModServerConfig.SPEC);

		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(this::onCommonSetup);
		ModTunnels.REGISTER.register(fml_bus);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(this::onItemTooltip);
	}

	public void onCommonSetup(FMLCommonSetupEvent e)
	{

	}

	public void onItemTooltip(ItemTooltipEvent e)
	{
		if (e.getItemStack().getItem() instanceof TunnelItem)
		{
			TunnelItem.getDefinition(e.getItemStack()).ifPresent(definition ->
			{
				if (definition instanceof NetworkTunnelDefinition)
				{
					e.getToolTip().add(1, Component.translatable("text.compactmachines_aet.tunnels.tooltip").withStyle(ChatFormatting.GRAY));
				}

			});
		}

	}

	public static void disconnect(TunnelWallEntity tunnelWall, CompactMachineBlockEntity compactMachine)
	{
		TunnelWallConnectionWrapper wrapper = ((TunnelWallExtension) tunnelWall).compactmachines_aet$getConnection();
		disconnect(wrapper);

		if (wrapper != null && wrapper.getCompactMachine() != compactMachine)
		{
			((CompactMachineExtension) wrapper.getCompactMachine()).compactmachines_aet$removeDense(tunnelWall.getBlockPos());
		}

	}

	public static void disconnect(ConnectionWrapper wrapper)
	{
		if (wrapper == null)
		{
			return;
		}

		IGridConnection connection = wrapper.getConnection();

		if (connection == null)
		{
			return;
		}

		connection.destroy();
		wrapper.setConnection(null);
	}

	public static void connect(CompactMachineBlockEntity compactMachine, TunnelWallEntity tunnelWall)
	{
		CompactMachineExtension cme = (CompactMachineExtension) compactMachine;
		TunnelWallExtension twe = (TunnelWallExtension) tunnelWall;
		ConnectionWrapper cmcw = cme.compactmachines_aet$getConnection(tunnelWall.getBlockPos());
		ConnectionWrapper twcw = twe.compactmachines_aet$getConnection();
		IGridConnection cmc = cmcw == null ? null : cmcw.getConnection();
		IGridConnection twc = twcw == null ? null : twcw.getConnection();

		if (cmc != twc || cmc == null || twc == null)
		{
			if (cmc != null)
			{
				cmc.destroy();
			}

			disconnect(tunnelWall, compactMachine);

			IGridNode cmn = ((IGridConnectedBlockEntity) compactMachine).getGridNode();
			IGridNode twn = ((IGridConnectedBlockEntity) tunnelWall).getGridNode();

			if (cmn == null || twn == null)
			{
				return;
			}

			IGridConnection connection = GridHelper.createConnection(cmn, twn);
			cme.compactmachines_aet$setConnection(tunnelWall.getBlockPos(), new CompactMachineConnectionWrapper(connection, tunnelWall));
			twe.compactmachines_aet$setConnection(new TunnelWallConnectionWrapper(connection, compactMachine));

			if (twe.compactmachines_aet$isDenseNode())
			{
				cme.compactmachines_aet$addDense(tunnelWall.getBlockPos());
			}

		}

	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MODID, path);
	}

}
