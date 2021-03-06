package extracells.util

import java.util

import appeng.api.AEApi
import extracells.integration.Integration.Mods
import extracells.integration.wct.WirelessCrafting
import extracells.item.WirelessTerminalType
import extracells.registries.{ItemEnum, PartEnum}
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer
import net.minecraft.item.ItemStack


object UniversalTerminal {
  val isMekLoaded = Mods.MEKANISMGAS.isEnabled
  val isThaLoaded = Mods.THAUMATICENERGISTICS.isEnabled
  val isWcLLoaded = Mods.WIRELESSCRAFTING.isEnabled
//  val arrayLength = {
//    var length = 2
//    if (isMekLoaded)
//      length += 1
//    if (isThaLoaded)
//      length += 1
//    if (isWcLLoaded)
//      length += 1
//    length
//  }

  val wirelessTerminals: Array[ItemStack] = {
    val terminals = new util.ArrayList[ItemStack]()
    var index: Int = 0;
    Option(AEApi.instance.definitions.items.wirelessTerminal.maybeStack(1)
        .orElse(null))
        .foreach( terminal => terminals.add(terminal))

    terminals.add(ItemEnum.FLUIDWIRELESSTERMINAL.getSizedStack(1))
    if (isMekLoaded) {
      terminals.add(ItemEnum.GASWIRELESSTERMINAL.getSizedStack(1))
    }
    //TODO:TE 1.10.2
    /*if(isThaLoaded) {
      terminals.update(next, ThaumaticEnergistics.getWirelessTerminal)
      next += 1
    }*/
    if (isWcLLoaded)
      terminals.add(WirelessCrafting.getCraftingTerminal)
    terminals.toArray(new Array[ItemStack](terminals.size()))
  }

  val terminals: Array[ItemStack] = {
    val terminals = new util.ArrayList[ItemStack]()
    Option(AEApi.instance.definitions.parts.terminal.maybeStack(1)
      .orElse(null))
      .foreach( terminal => terminals.add(terminal))
    terminals.add(ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal))
    if (isMekLoaded) {
      terminals.add(ItemEnum.PARTITEM.getDamagedStack(PartEnum.GASTERMINAL.ordinal))
      //TODO:TE 1.10.2
      /*if(isThaLoaded)
        terminals.update(3, ThaumaticEnergistics.getTerminal)*/
      /*}else if(isThaLoaded)
      terminals.update(2, ThaumaticEnergistics.getTerminal)*/
    }
    terminals.toArray(new Array[ItemStack](terminals.size()))
  }

  def isTerminal(stack: ItemStack): Boolean = {
    if (stack == null)
      return false
    val item = stack.getItem
    val meta = stack.getItemDamage
    if (item == null)
      return false
    val aeterm = AEApi.instance.definitions.parts.terminal.maybeStack(1).orElse(null)
    if (aeterm != null && item == aeterm.getItem && meta == aeterm.getItemDamage)
      return true
    val aetermfluid = AEApi.instance.definitions.parts.fluidTerminal.maybeStack(1).orElse(null)
    if (aetermfluid != null && item == aetermfluid.getItem && meta == aetermfluid.getItemDamage)
      return true
    val ecterm = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal)
    if (item == ecterm.getItem && meta == ecterm.getItemDamage)
      return true
    val ectermgas = ItemEnum.PARTITEM.getDamagedStack(PartEnum.GASTERMINAL.ordinal)
    if (item == ectermgas.getItem && meta == ectermgas.getItemDamage)
      return true
    val aetermcrafting = AEApi.instance.definitions.parts.craftingTerminal.maybeStack(1).orElse(null)
    if (aetermcrafting != null && item == aetermcrafting.getItem && meta == aetermcrafting.getItemDamage)
      return true
    /*if(Mods.THAUMATICENERGISTICS.isEnabled){
      val thterm = ThaumaticEnergistics.getTerminal
      if(item == thterm.getItem && meta == thterm.getItemDamage)
        return true
    }*/
    false
  }

  def isWirelessTerminal(stack: ItemStack): Boolean = {
    if (stack == null)
      return false

    val item = stack.getItem
    val meta = stack.getItemDamage

    if (item == null)
      return false
    val aeterm = AEApi.instance.definitions.items.wirelessTerminal.maybeStack(1).orElse(null)
    if (aeterm != null && item == aeterm.getItem && meta == aeterm.getItemDamage)
      return true
    val ecterm = ItemEnum.FLUIDWIRELESSTERMINAL.getDamagedStack(0)
    if (item == ecterm.getItem && meta == ecterm.getItemDamage)
      return true
    val ectermgas = ItemEnum.GASWIRELESSTERMINAL.getDamagedStack(0)
    if (item == ectermgas.getItem && meta == ectermgas.getItemDamage)
      return true
    /*if(Mods.THAUMATICENERGISTICS.isEnabled){
      val thterm = ThaumaticEnergistics.getWirelessTerminal
      if(item == thterm.getItem && meta == thterm.getItemDamage)
        return true
    }*/
    if(isWcLLoaded){
      val wcTerm = WirelessCrafting.getCraftingTerminal
      if(item == wcTerm.getItem && meta == wcTerm.getItemDamage)
        return true
    }
    false
  }

  def getTerminalType(stack: ItemStack): WirelessTerminalType = {
    if (stack == null)
      return null
    val item = stack.getItem
    val meta = stack.getItemDamage
    if (item == null)
      return null
    val aeterm = AEApi.instance.definitions.parts.terminal.maybeStack(1).orElse(null)
    if (aeterm != null && item == aeterm.getItem && meta == aeterm.getItemDamage)
      return WirelessTerminalType.ITEM
    val aetermfluid = AEApi.instance.definitions.parts.fluidTerminal.maybeStack(1).orElse(null)
    if (aetermfluid != null && item == aetermfluid.getItem && meta == aetermfluid.getItemDamage)
      return WirelessTerminalType.FLUID
    val ecterm = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal)
    if (item == ecterm.getItem && meta == ecterm.getItemDamage)
      return WirelessTerminalType.FLUID
    val ectermgas = ItemEnum.PARTITEM.getDamagedStack(PartEnum.GASTERMINAL.ordinal)
    if (item == ectermgas.getItem && meta == ectermgas.getItemDamage)
      return WirelessTerminalType.GAS
    /*if(Mods.THAUMATICENERGISTICS.isEnabled){
      val thterm = ThaumaticEnergistics.getTerminal
      if(item == thterm.getItem && meta == thterm.getItemDamage)
        return TerminalType.ESSENTIA
    }*/
    val aeterm2 = AEApi.instance.definitions.items.wirelessTerminal.maybeStack(1).orElse(null)
    if (aeterm2 != null && item == aeterm2.getItem && meta == aeterm2.getItemDamage)
      return WirelessTerminalType.ITEM
    val ecterm2 = ItemEnum.FLUIDWIRELESSTERMINAL.getDamagedStack(0)
    if (item == ecterm2.getItem && meta == ecterm2.getItemDamage)
      return WirelessTerminalType.FLUID
    val ectermgas2 = ItemEnum.GASWIRELESSTERMINAL.getDamagedStack(0)
    if (item == ectermgas2.getItem && meta == ectermgas2.getItemDamage)
      return WirelessTerminalType.GAS
    /*if(Mods.THAUMATICENERGISTICS.isEnabled){
      val thterm = ThaumaticEnergistics.getWirelessTerminal
      if(item == thterm.getItem && meta == thterm.getItemDamage)
        return TerminalType.ESSENTIA
    }*/
    if(isWcLLoaded){
      val aetermcrafting = AEApi.instance.definitions.parts.craftingTerminal.maybeStack(1).orElse(null)
      if (aetermcrafting != null && item == aetermcrafting.getItem && meta == aetermcrafting.getItemDamage)
        return WirelessTerminalType.CRAFTING
      val wcTerm = WirelessCrafting.getCraftingTerminal
      if(item == wcTerm.getItem && meta == wcTerm.getItemDamage)
        return WirelessTerminalType.CRAFTING
    }
    null
  }

}
