package cn.cyanbukkit.points.gui

import cn.cyanbukkit.points.CyanPoints
import cn.cyanbukkit.points.data.LoaderData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

object PointsMenuListener : Listener {

    val task = mutableMapOf<Player, Int>()

    val limitedIcon = ItemStack(Material.TRIPWIRE_HOOK).apply {
        itemMeta = itemMeta.apply {
            displayName = "§6支付限制(§a限制中§6)"
            lore = listOf("§7点击开启或关闭支付限制")
        }
    }


    val limitIcon = ItemStack(Material.TRIPWIRE_HOOK).apply {
        itemMeta = itemMeta.apply {
            displayName = "§6支付限制(§a限制关闭§6)"
            lore = listOf("§7点击开启或关闭支付限制")
        }
    }


    val deadbeatedIcon = ItemStack(Material.BARRIER).apply {
        itemMeta = itemMeta.apply {
            displayName = "§c你的信用太差了"
        }
    }

    val deadbeatIcon = ItemStack(Material.BARRIER).apply {
        itemMeta = itemMeta.apply {
            displayName = "§a你的信用良好"
        }
    }


    @EventHandler
    fun onInventoryClick(e: org.bukkit.event.inventory.InventoryClickEvent) {
        val p = e.whoClicked as Player
        if (e.view.title == "§6点券设置") {
            e.isCancelled = true
            if (e.currentItem == null) return
            if (e.currentItem == limitedIcon) {
                LoaderData.setPayLimit(p.uniqueId.toString(), false)
                e.view.setItem(11, limitIcon)
                p.sendMessage("§a支付限制已关闭")
            } else if (e.currentItem == limitIcon) {
                LoaderData.setPayLimit(p.uniqueId.toString(), true)
                e.view.setItem(11, limitedIcon)
                p.sendMessage("§a支付限制已开启")
            }
        }
    }


    @EventHandler
    fun onInventoryClose(e: org.bukkit.event.inventory.InventoryCloseEvent) {
        val p = e.player as Player
        if (e.view.title == "§6点券设置") {
            Bukkit.getScheduler().cancelTask(task[p]!!)
        }
    }


    fun Player.openPointsMenu() {
        val inv = Bukkit.createInventory(null, 27, "§6点券设置")


        val id = Bukkit.getScheduler().runTaskTimerAsynchronously(CyanPoints.instance, {
            if (LoaderData.isPayLimit(uniqueId.toString())) {
                inv.setItem(11, limitedIcon)
            } else {
                inv.setItem(11, limitIcon)
            }


            if (LoaderData.isDeadbeat(uniqueId.toString())) {
                inv.setItem(15, deadbeatedIcon)
            } else {
                inv.setItem(15, deadbeatIcon)
            }


        }, 0, 20L).taskId


        task[this] = id

        this.openInventory(inv)
    }

}


