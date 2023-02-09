package net.mcreator.sensible_sleep.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.sensible_sleep.network.SensibleSleepModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class SaveSleepTimeProcedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != event.getPlayer().getUsedItemHand())
			return;
		execute(event, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		execute(null, world, x, y, z);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(new BlockPos(x, y, z))).is(BlockTags.create(new ResourceLocation("minecraft:beds")))) {
			SensibleSleepModVariables.MapVariables.get(world).sleeptime = world.dayTime();
			SensibleSleepModVariables.MapVariables.get(world).syncData(world);
			if (world.getLevelData().isRaining() || world.getLevelData().isThundering()) {
				SensibleSleepModVariables.MapVariables.get(world).rainingSleep = true;
				SensibleSleepModVariables.MapVariables.get(world).syncData(world);
			} else {
				SensibleSleepModVariables.MapVariables.get(world).rainingSleep = false;
				SensibleSleepModVariables.MapVariables.get(world).syncData(world);
			}
		}
	}
}
