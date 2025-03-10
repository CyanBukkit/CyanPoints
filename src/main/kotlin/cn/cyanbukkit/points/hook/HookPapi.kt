package cn.cyanbukkit.points.hook

import cn.cyanbukkit.points.data.LoaderData
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

object HookPapi : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "points"
    }

    override fun getAuthor(): String {
        return "CyanBukkit"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String {
        return when (params) { // 获取点券
            "points" -> LoaderData.get(player.uniqueId.toString()).toString()
            "islimit" -> if (LoaderData.isPayLimit(player.uniqueId.toString())) "§a开启了支付限制" else "§a未开启支付限制"
            "isdeadbeat" -> if (LoaderData.isDeadbeat(player.uniqueId.toString())) "§c极差已成为老赖" else "§a良好"
            "il" -> if (LoaderData.isPayLimit(player.uniqueId.toString())) "true" else "false"
            "idb" -> if (LoaderData.isDeadbeat(player.uniqueId.toString())) "true" else "false"
            else -> ""
        }
    }


    override fun getPlaceholders(): MutableList<String> {
        return mutableListOf("points", "islimit", "isdeadbeat", "il", "idb")
    }


}