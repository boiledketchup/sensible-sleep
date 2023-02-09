
package net.mcreator.sensible_sleep.enchantment;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;

public class HypersomniaEnchantment extends Enchantment {
	public HypersomniaEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, slots);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}
}
