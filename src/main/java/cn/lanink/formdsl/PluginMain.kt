package cn.lanink.formdsl

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.plugin.PluginBase

class PluginMain : PluginBase() {
    override fun onEnable() {
        logger.info("Form DSL loaded.")
    }

    override fun onDisable() {
        logger.info("Form DSL disabled.")
    }

    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        if (sender is Player) {
            when (args?.get(0)) {
                "1" -> exampleSimple(sender)
                "2" -> exampleCustom(sender)
                "3" -> exampleModal(sender)
                else -> sender.sendMessage("args should be 1, 2, 3")
            }
        }else{
            sender?.sendMessage("use it in the game")
        }
        return true
    }
}