package net.daplumer.data_modification_utils.mod_registries.registering_functions

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.vehicle.AbstractBoatEntity
import net.minecraft.item.BoatItem
import net.minecraft.item.HangingSignItem
import net.minecraft.item.Item
import net.minecraft.item.SignItem
import net.minecraft.item.SmithingTemplateItem
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.Rarity

fun BOAT(type:EntityType<out AbstractBoatEntity>) = { settings: Item.Settings -> BoatItem(type,settings)}
fun SIGN(normal:Block,wall:Block) = {settings:Item.Settings -> SignItem(normal,wall,settings)}
fun HANGING_SIGN(normal:Block,wall:Block) = {settings:Item.Settings -> HangingSignItem(normal,wall,settings) }
fun SPAWN_EGG(type: EntityType<out MobEntity>) = {settings:Item.Settings -> SpawnEggItem(type, settings)}
fun ARMOR_TRIM(rarity: Rarity) = {settings:Item.Settings -> SmithingTemplateItem.of(settings.rarity(rarity))}
