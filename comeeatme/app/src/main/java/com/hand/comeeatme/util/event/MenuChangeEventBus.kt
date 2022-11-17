package com.hand.comeeatme.util.event

import com.hand.comeeatme.view.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

class MenuChangeEventBus {
    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu: MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }
}