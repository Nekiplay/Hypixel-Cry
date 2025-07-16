package com.nekiplay.hypixelcry.features.modules

open class BindableClientModule() : ClientModule() {
    open fun getKeybind(): Int { return -1 }
    open fun press() {}
    open fun release() {}
    open fun repeat() {}
}