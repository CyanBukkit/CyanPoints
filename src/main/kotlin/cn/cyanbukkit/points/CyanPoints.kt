package cn.cyanbukkit.points

import cn.cyanbukkit.points.command.PointsCommand
import cn.cyanbukkit.points.data.LoaderData
import cn.cyanbukkit.points.gui.PointsMenuListener
import cn.cyanbukkit.points.hook.HookPapi
import cn.cyanbukkit.points.hook.VaultHook
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class CyanPoints : JavaPlugin(), Listener {

    companion object {
        lateinit var instance: CyanPoints

        fun Command.register() {
            val pluginManagerClazz = instance.server.pluginManager.javaClass
            val field = pluginManagerClazz.getDeclaredField("commandMap")
            field.isAccessible = true
            val commandMap = field.get(instance.server.pluginManager) as SimpleCommandMap
            commandMap.register(instance.name, this)
        }

    }

    override fun onEnable() {
        // Start
        instance = this
        // 加载配置文件
        saveDefaultConfig()
        LoaderData.link()
        PointsCommand().register()
        HookPapi.register()
        // 注册Vault经济系统
        if (server.pluginManager.getPlugin("Vault") != null) {
            server.servicesManager.register(Economy::class.java, VaultHook(), this, org.bukkit.plugin.ServicePriority.High)
            logger.info("成功挂钩Vault经济系统")
        }
        Bukkit.getPluginManager().registerEvents(PointsMenuListener, this)
        server.pluginManager.registerEvents(this, this)
        logger.info("CyanPoints is enabled!")
        logger.info("CyanPoints by CyanBukkit Code")
    }

    override fun onDisable() {
        // 卸载Vault经济系统
        server.servicesManager.unregisterAll(this)
        logger.info("CyanPoints is disabled!")
        logger.info("CyanPoints by CyanBukkit Code")
    }


    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        // 加载玩家数据
        val uuid = e.player
        if (LoaderData.inTable(uuid.uniqueId.toString())) return
        LoaderData.set(uuid.uniqueId.toString(), 10)
    }

}