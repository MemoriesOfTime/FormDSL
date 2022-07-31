package cn.lanink.formdsl

import cn.lanink.gamecore.hotswap.ModuleBase

class ModuleMain : ModuleBase() {
    override fun onEnable() {
        logger.info("Form DSL loaded.")
    }

    override fun onDisable() {
        logger.info("Form DSL disabled.")
    }
}