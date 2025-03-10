package cn.cyanbukkit.points.data

import cn.cyanbukkit.points.data.LoaderData.isDeadbeat
import cn.cyanbukkit.points.data.LoaderData.isPayLimit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PlayerPointsAPI {

    /**
     * 给玩家点数
     * @param p 玩家
     * @param amount 数量
     * @return 是否成功
     */
    fun give(p: Player, amount: Int): Boolean {// 给玩家点券无视限制
        try {
            LoaderData.give(p.uniqueId.toString(), amount)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }



    fun give(op: OfflinePlayer, amount: Int): Boolean {// 给玩家点券无视限制
        try {
            LoaderData.give(op.uniqueId.toString(), amount)
            return true
        } catch (e: Exception) {
            // 在别的线程中报错
            e.printStackTrace()
            return false
        }
    }




    /**
     * 拿走有限制老赖识别
     * @param p 玩家
     * @param amount 数量
     * @return 是否成功
     */
    fun take(p: Player, amount: Int): Boolean {
        val sourceUUID = p.uniqueId.toString()
        if (isDeadbeat(sourceUUID)) { // 先检测是不是长期欠钱不还的老赖
            p.sendMessage("§c你是个老赖赶紧还钱！")
            return false
        }
        val now = LoaderData.get(sourceUUID)
        if (isPayLimit(sourceUUID)) { // 是不是付款人是不是开了支付限制
            if (now <= amount) {
                return false
            } else {
                LoaderData.set(p.uniqueId.toString(), now - amount)
                return true
            }
        } else {
            LoaderData.set(p.uniqueId.toString(), now - amount)
            return true
        }
    }


    fun take(op: OfflinePlayer, amount: Int): Boolean {
        val sourceUUID = op.uniqueId.toString()
        val now = LoaderData.get(sourceUUID)
        LoaderData.set(op.uniqueId.toString(), now - amount)
        return true
    }



    /**
     * 查看玩家的点数
     * @param p 玩家
     * @return 点数
     */
    fun look(p: Player): Int {
        return LoaderData.get(p.uniqueId.toString())
    }

    fun look(op: OfflinePlayer): Int {
        return LoaderData.get(op.uniqueId.toString())
    }



    /**
     * 用于显示的格式
     * @param p 玩家
     * @return 显示格式
     */
    fun lookFormatted(p: Player): String {
        return "§6${look(p)}§e点券"
    }

    fun lookFormatted(op: OfflinePlayer): String {
        return "§6${look(op)}§e点券"
    }



    /**
     * 用于显示的简短格式
     * @param p 玩家
     * @return 简短的显示格式
     */
    fun lookShorthand(p: Player): String {
        return "§6${look(p)}§e点"
    }

    fun lookShorthand(op: OfflinePlayer): String {
        return "§6${look(op)}§e点"
    }



    /**
     * 老赖管理切换
     * @param p 玩家
     */
    fun toggleDeadbeat(p: Player) {
        if (isDeadbeat(p.uniqueId.toString())) {
            LoaderData.setDeadbeat(p.uniqueId.toString(), false)
            p.sendMessage("§a你已经不再是老赖，做一个合法公民从我做起")
        } else {
            LoaderData.setDeadbeat(p.uniqueId.toString(), true)
            p.sendMessage("§c由于你的行为违反服务器规定，故服务器管理组将你设为“老赖”， 你将被服务器的一些玩法限行！")
        }
    }

    fun toggleDeadbeat(op: OfflinePlayer) {
        if (isDeadbeat(op.uniqueId.toString())) {
            LoaderData.setDeadbeat(op.uniqueId.toString(), false)
        } else {
            LoaderData.setDeadbeat(op.uniqueId.toString(), true)
        }
    }




    fun togglePayLimit(p: Player) {
        if (isPayLimit(p.uniqueId.toString())) {
            LoaderData.setPayLimit(p.uniqueId.toString(), false)
        } else {
            LoaderData.setPayLimit(p.uniqueId.toString(), true)
        }
    }


    fun togglePayLimit(op: OfflinePlayer) {
        if (isPayLimit(op.uniqueId.toString())) {
            LoaderData.setPayLimit(op.uniqueId.toString(), false)
        } else {
            LoaderData.setPayLimit(op.uniqueId.toString(), true)
        }
    }



    /**
     * 转账
     * @param source 转账人
     * @param target 收款人
     * @param amount 数量
     * @return 是否成功
     */
    fun pay(source: Player, target: Player, amount: Int): Boolean {
        // 限制时的转账
        val sourceUUID = source.uniqueId.toString()
        if (isDeadbeat(sourceUUID)) {
            source.sendMessage("§c你是个老赖赶紧还钱！")
            return false
        }
        if (isPayLimit(sourceUUID)) {
            if (LoaderData.get(source.uniqueId.toString()) >= amount) {
                take(source, amount)
                give(target, amount)
                return true
            } else {
                return false
            }
        } else {
            take(source, amount)
            give(target, amount)
            return true
        }
    }







    /**
     * 设置玩家的点数
     * @param p 玩家
     * @param amount 数量
     * @return 是否成功
     */
    fun set(p: Player, amount: Int): Boolean {
        try {
            LoaderData.set(p.uniqueId.toString(), amount)
            return true
        } catch (e: Exception) {
            return false
        }
    }



    fun set(op: OfflinePlayer, amount: Int): Boolean {
        try {
            LoaderData.set(op.uniqueId.toString(), amount)
            return true
        } catch (e: Exception) {
            return false
        }
    }


    /**
     * 是老赖么？
     * @param p 玩家
     * @return 是否是老赖
     */
    fun isDeadbeat(p: Player): Boolean {
        return isDeadbeat(p.uniqueId.toString())
    }




    fun isDeadbeat(op: OfflinePlayer): Boolean {
        return isDeadbeat(op.uniqueId.toString())
    }




    /**
     * 是支付限制么？
     * @param p 玩家
     * @return 是否支付限制
     */
    fun isPayLimit(p: Player): Boolean {
        return isPayLimit(p.uniqueId.toString())
    }


    fun isPayLimit(op: OfflinePlayer): Boolean {
        return isPayLimit(op.uniqueId.toString())
    }




    /**
     * 重置玩家的点数
     * @param p 玩家
     * @return 是否成功
     */
    fun reset(p: Player): Boolean {
        try {
            LoaderData.set(p.uniqueId.toString(), 0)
            return true
        } catch (e: Exception) {
            return false
        }
    }


    fun reset(op: OfflinePlayer): Boolean {
        try {
            LoaderData.set(op.uniqueId.toString(), 0)
            return true
        } catch (e: Exception) {
            return false
        }
    }




}