package net.mcreator.sensible_sleep.procedures;

import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.mcreator.sensible_sleep.network.SensibleSleepModVariables;
import net.mcreator.sensible_sleep.init.SensibleSleepModEnchantments;

import javax.annotation.Nullable;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

@Mod.EventBusSubscriber
public class NightmareProcedure {
	@SubscribeEvent
	public static void onEntityEndSleep(PlayerWakeUpEvent event) {
		execute(event, event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean nightmare = false;
		boolean safe = false;
		boolean villager = false;
		nightmare = false;
		safe = false;
		villager = false;
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream()
					.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (entityiterator instanceof Villager) {
					if (!(entityiterator instanceof LivingEntity _livEnt ? _livEnt.isSleeping() : false)
							&& !(entityiterator instanceof LivingEntity _livEnt ? _livEnt.isBaby() : false)) {
						villager = true;
						if (entityiterator instanceof LivingEntity _entity)
							_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12000, 1, (false), (true)));
						break;
					}
				}
			}
		}
		if (!(EnchantmentHelper.getItemEnchantmentLevel(SensibleSleepModEnchantments.HYPERSOMNIA.get(),
				(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) != 0)) {
			if (!world.canSeeSkyFromBelowWater(new BlockPos(x, y + 1, z)) && 7 < world.getMaxLocalRawBrightness(new BlockPos(x, y, z))) {
				safe = true;
			}
			if (!safe && !villager) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(24 / 2d), e -> true).stream()
							.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMobType() == MobType.UNDEAD : false)
								|| (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMobType() == MobType.ARTHROPOD : false)) {
							nightmare = true;
							{
								Entity _ent = entityiterator;
								_ent.teleportTo(x, y, z);
								if (_ent instanceof ServerPlayer _serverPlayer)
									_serverPlayer.connection.teleport(x, y, z, _ent.getYRot(), _ent.getXRot());
							}
							break;
						}
					}
				}
			}
			if (!safe) {
				if (entity instanceof LivingEntity _entity)
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
				if (!villager && !nightmare) {
					if (entity instanceof Player _player && !_player.level.isClientSide())
						_player.displayClientMessage(new TextComponent((new TranslatableComponent("message.sleep.unsafe").getString())), (true));
				}
			}
			if (nightmare && !villager) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, new BlockPos(x, y, z),
								ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_wooden_door")), SoundSource.NEUTRAL,
								1, 1);
					} else {
						_level.playLocalSound(x, y, z,
								ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_wooden_door")), SoundSource.NEUTRAL,
								1, 1, false);
					}
				}
				world.getLevelData().setRaining(SensibleSleepModVariables.MapVariables.get(world).rainingSleep);
				if ((world.isClientSide()
						? Minecraft.getInstance().getConnection().getOnlinePlayers().size()
						: ServerLifecycleHooks.getCurrentServer().getPlayerCount()) == 1) {
					if (world instanceof ServerLevel _level)
						_level.setDayTime((int) SensibleSleepModVariables.MapVariables.get(world).sleeptime);
				}
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent((new TranslatableComponent("message.sleep.nightmare").getString())), (true));
			}
			if (villager && !(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.HERO_OF_THE_VILLAGE) : false)) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, new BlockPos(x, y, z),
								ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.villager.no")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.villager.no")),
								SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.ANGRY_VILLAGER, x, y, z, 20, 0.5, 0.5, 0.5, 0.1);
				world.getLevelData().setRaining(SensibleSleepModVariables.MapVariables.get(world).rainingSleep);
				if ((world.isClientSide()
						? Minecraft.getInstance().getConnection().getOnlinePlayers().size()
						: ServerLifecycleHooks.getCurrentServer().getPlayerCount()) == 1) {
					if (world instanceof ServerLevel _level)
						_level.setDayTime((int) SensibleSleepModVariables.MapVariables.get(world).sleeptime);
				}
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent((new TranslatableComponent("message.sleep.villagerbed").getString())), (true));
			}
		}
	}
}
