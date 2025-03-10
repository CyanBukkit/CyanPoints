package cn.cyanbukkit.points.hook

import cn.cyanbukkit.points.CyanPoints
import cn.cyanbukkit.points.data.PlayerPointsAPI
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class VaultHook : Economy {
    override fun isEnabled(): Boolean = true

    override fun getName(): String = "CyanPoints"

    override fun hasBankSupport(): Boolean = false

    override fun fractionalDigits(): Int = 0

    override fun format(amount: Double): String = "${amount.toInt()} 铜币"

    override fun currencyNamePlural(): String = "铜币"

    override fun currencyNameSingular(): String = "铜币"

    override fun hasAccount(player: String): Boolean = true

    override fun hasAccount(player: OfflinePlayer): Boolean = true

    override fun hasAccount(player: String, worldName: String): Boolean = true

    override fun hasAccount(player: OfflinePlayer, worldName: String): Boolean = true

    override fun getBalance(player: String): Double = 
        Bukkit.getOfflinePlayer(player).let { PlayerPointsAPI.look(it).toDouble() }

    override fun getBalance(player: OfflinePlayer): Double = 
        PlayerPointsAPI.look(player).toDouble()

    override fun getBalance(player: String, world: String): Double = getBalance(player)

    override fun getBalance(player: OfflinePlayer, world: String): Double = getBalance(player)

    override fun has(player: String, amount: Double): Boolean =
        getBalance(player) >= amount

    override fun has(player: OfflinePlayer, amount: Double): Boolean =
        getBalance(player) >= amount

    override fun has(player: String, worldName: String, amount: Double): Boolean =
        has(player, amount)

    override fun has(player: OfflinePlayer, worldName: String, amount: Double): Boolean =
        has(player, amount)

    override fun withdrawPlayer(player: String, amount: Double): EconomyResponse {
        val offlinePlayer = Bukkit.getOfflinePlayer(player)
        return if (PlayerPointsAPI.take(offlinePlayer, amount.toInt())) {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "扣除点券失败")
        }
    }

    override fun withdrawPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        return if (PlayerPointsAPI.take(player, amount.toInt())) {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "扣除点券失败")
        }
    }

    override fun withdrawPlayer(player: String, worldName: String, amount: Double): EconomyResponse =
        withdrawPlayer(player, amount)

    override fun withdrawPlayer(player: OfflinePlayer, worldName: String, amount: Double): EconomyResponse =
        withdrawPlayer(player, amount)

    override fun depositPlayer(player: String, amount: Double): EconomyResponse {
        val offlinePlayer = Bukkit.getOfflinePlayer(player)
        return if (PlayerPointsAPI.give(offlinePlayer, amount.toInt())) {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "给予点券失败")
        }
    }

    override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        return if (PlayerPointsAPI.give(player, amount.toInt())) {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null)
        } else {
            EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "给予点券失败")
        }
    }

    override fun depositPlayer(player: String, worldName: String, amount: Double): EconomyResponse =
        depositPlayer(player, amount)

    override fun depositPlayer(player: OfflinePlayer, worldName: String, amount: Double): EconomyResponse =
        depositPlayer(player, amount)

    override fun createBank(name: String, player: String): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun createBank(name: String, player: OfflinePlayer): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun deleteBank(name: String): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun bankBalance(name: String): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun bankHas(name: String, amount: Double): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun bankWithdraw(name: String, amount: Double): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun bankDeposit(name: String, amount: Double): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun isBankOwner(name: String, playerName: String): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun isBankOwner(name: String, player: OfflinePlayer): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun isBankMember(name: String, playerName: String): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun isBankMember(name: String, player: OfflinePlayer): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "不支持银行功能")

    override fun getBanks(): List<String> = emptyList()

    override fun createPlayerAccount(player: String): Boolean = true

    override fun createPlayerAccount(player: OfflinePlayer): Boolean = true

    override fun createPlayerAccount(player: String, worldName: String): Boolean = true

    override fun createPlayerAccount(player: OfflinePlayer, worldName: String): Boolean = true
}