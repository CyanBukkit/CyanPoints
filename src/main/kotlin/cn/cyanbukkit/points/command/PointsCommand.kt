package cn.cyanbukkit.points.command

import cn.cyanbukkit.points.CyanPoints
import cn.cyanbukkit.points.data.PlayerPointsAPI
import cn.cyanbukkit.points.gui.PointsMenuListener
import cn.cyanbukkit.points.gui.PointsMenuListener.openPointsMenu
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PointsCommand : Command("points") {
    override fun execute(sender: CommandSender, p1: String, arg: Array<out String>): Boolean { //
        if (arg.isEmpty()) {
            sender.sendMessage(help)
            return true
        }
        if (arg[0].equals("help", true)) {
            sender.sendMessage(help)
            return true
        }

        when {
            arg[0].equals("set", true) -> {
                if (!sender.hasPermission("points.set")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 3) {
                    sender.sendMessage("§c/points set <player> <amount>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                val amount = arg[2].toIntOrNull()
                if (amount == null) {
                    sender.sendMessage("§c/points set <player> <amount>")
                    return true
                }
                if (player != null) {
                    if (PlayerPointsAPI.set(player, amount)) {
                        sender.sendMessage("§a成功设置")
                    } else {
                        sender.sendMessage("§c设置失败")
                    }
                } else {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        if (PlayerPointsAPI.set(offlinePlayer, amount)) {
                            sender.sendMessage("§a成功设置")
                        } else {
                            sender.sendMessage("§c设置失败")
                        }
                    }
                }
            }


            arg[0].equals("menu", true) -> {
                if (sender !is Player) {
                    return true
                }
                val player = sender
                player.openPointsMenu()
            }


            arg[0].equals("give", true) -> {
                if (!sender.hasPermission("points.give")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 3) {
                    sender.sendMessage("§c/points give <player> <amount>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                val amount = arg[2].toIntOrNull()
                if (amount == null) {
                    sender.sendMessage("§c/points give <player> <amount>")
                    return true
                }
                if (player != null) {
                    if (PlayerPointsAPI.give(player, amount)) {
                        sender.sendMessage("§a成功给予")
                    } else {
                        sender.sendMessage("§c给予失败")
                    }
                } else {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        if (PlayerPointsAPI.give(offlinePlayer, amount)) {
                            sender.sendMessage("§a成功给予")
                        } else {
                            sender.sendMessage("§c给予失败")
                        }
                    }
                }
            }

            arg[0].equals("take", true) -> {
                if (!sender.hasPermission("points.take")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 3) {
                    sender.sendMessage("§c/points take <player> <amount>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                val amount = arg[2].toIntOrNull()
                if (amount == null) {
                    sender.sendMessage("§c/points take <player> <amount>")
                    return true
                }
                if (player != null) {
                    if (PlayerPointsAPI.take(player, amount)) {
                        sender.sendMessage("§a成功扣除")
                    } else {
                        sender.sendMessage("§c扣除失败")
                    }
                } else {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        if (PlayerPointsAPI.take(offlinePlayer, amount)) {
                            sender.sendMessage("§a成功扣除")
                        } else {
                            sender.sendMessage("§c扣除失败")
                        }
                    }
                }
            }

            arg[0].equals("look", true) -> {
                if (!sender.hasPermission("points.look")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 2) {
                    sender.sendMessage("§c/points look <player>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                if (player == null) {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        sender.sendMessage("§e${offlinePlayer.name} §b的点数是 §e${PlayerPointsAPI.look(offlinePlayer)}")
                    }
                } else {
                    sender.sendMessage("§e${player.name} §b的点数是 §e${PlayerPointsAPI.look(player)}")
                }
            }

            arg[0].equals("pay", true) -> {
                if (arg.size < 3) {
                    sender.sendMessage("§c/points pay <player> <amount>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                val amount = arg[2].toIntOrNull()
                if (amount == null) {
                    sender.sendMessage("§c/points pay <player> <amount>")
                    return true
                }
                if (sender !is Player) {
                    return true
                }
                PlayerPointsAPI.pay(sender,player,amount)
            }

            arg[0].equals("reset", true) -> {
                if (!sender.hasPermission("points.reset")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 2) {
                    sender.sendMessage("§c/points reset <player>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                if (player != null) {
                    if (PlayerPointsAPI.set(player, 0)) {
                        sender.sendMessage("§a成功重置")
                    } else {
                        sender.sendMessage("§c重置失败")
                    }
                } else {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        if (PlayerPointsAPI.set(offlinePlayer, 0)) {
                            sender.sendMessage("§a成功重置")
                        } else {
                            sender.sendMessage("§c重置失败")
                        }
                    }
                }
            }

            arg[0].equals("reload", true) -> {
                if (!sender.hasPermission("points.setall")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 2) {
                    sender.sendMessage("§c/points setall <amount>")
                    return true
                }
                val amount = arg[1].toIntOrNull()
                if (amount == null) {
                    sender.sendMessage("§c/points setall <amount>")
                    return true
                }
                CyanPoints.instance.reloadConfig()
                CyanPoints.instance.onEnable()
                CyanPoints.instance.logger.info("CyanPoints is reloaded!")
            }

            // setdeadbeat
            arg[0].equals("setdeadbeat", true) -> {
                if (!sender.hasPermission("points.setdeadbeat")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 2) {
                    sender.sendMessage("§c/points setdeadbeat <player>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                if (player != null) {
                    PlayerPointsAPI.toggleDeadbeat(player)
                    sender.sendMessage("§a成功设置")
                } else {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        PlayerPointsAPI.toggleDeadbeat(offlinePlayer)
                        sender.sendMessage("§a成功设置")
                    }
                }
            }

            // setlimit
            arg[0].equals("setlimit", true) -> {
                if (!sender.hasPermission("points.setlimit")) {
                    sender.sendMessage("§c你没有权限")
                    return true
                }
                if (arg.size < 2) {
                    sender.sendMessage("§c/points setlimit <player>")
                    return true
                }
                val player = Bukkit.getPlayer(arg[1])
                if (player != null) {
                    PlayerPointsAPI.togglePayLimit(player)
                    sender.sendMessage("§a成功设置")
                } else {
                    val offlinePlayer = Bukkit.getOfflinePlayer(arg[1])
                    offlinePlayer?.let {
                        PlayerPointsAPI.togglePayLimit(offlinePlayer)
                        sender.sendMessage("§a成功设置")
                    }
                }
            }


            else -> {
                sender.sendMessage(help)
            }

        }

        return true
    }


    private val help = """
        |------------------+- CyanPoints -+------------------
        | §b/points help  §e帮助
        | §b/points menu  §e菜单
        | §b/points set <player> <amount>  §e设置玩家的点数
        | §b/points give <player> <amount>  §e给玩家点数
        | §b/points take <player> <amount>  §e扣除玩家点数
        | §b/points look <player>  §e查看玩家点数
        | §b/points pay <player> <amount>  §e自己给玩家点数
        | §b/points reset <player>  §e重置玩家点数
        | §b/points setall <amount>  §e设置所有玩家点数
        | §b/points giveall <amount>  §e给所有玩家点数
        | §b/points reload  §e重载配置文件
        | §b/points setdeadbeat <player>  §e设置玩家为老赖
        | §b/points unsetdeadbeat <player>  §e取消玩家老赖
        | §b/points setlimit <player> §e设置玩家的转账限制模式
        | §b/points unsetlimit <player> §e取消玩家的转账限制模式
        |----------------------------------------------------
    """.trimIndent()


    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        if (args.size == 1) {
            return mutableListOf("help", "set", "give", "take", "look", "pay", "reset", "reload", "setdeadbeat", "unsetdeadbeat", "setlimit", "unsetlimit", "menu")
        }
        if (args.size == 2) {
            return Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
        }
        return mutableListOf()
    }
}