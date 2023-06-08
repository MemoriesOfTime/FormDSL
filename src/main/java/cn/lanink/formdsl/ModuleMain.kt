@file:Suppress("UNUSED")
package cn.lanink.formdsl

import cn.lanink.gamecore.hotswap.ModuleBase

class ModuleMain : ModuleBase() {
    override fun onEnable() {
        logger.info("Module FormDSL loaded.")
    }

    override fun onDisable() {
        logger.info("Module FormDSL disabled.")
    }
}