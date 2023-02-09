
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.sensible_sleep.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.sensible_sleep.enchantment.HypersomniaEnchantment;
import net.mcreator.sensible_sleep.SensibleSleepMod;

public class SensibleSleepModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SensibleSleepMod.MODID);
	public static final RegistryObject<Enchantment> HYPERSOMNIA = REGISTRY.register("hypersomnia", () -> new HypersomniaEnchantment());
}
