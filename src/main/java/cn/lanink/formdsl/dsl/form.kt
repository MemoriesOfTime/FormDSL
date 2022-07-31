@file:Suppress("UNUSED")
package cn.lanink.formdsl.dsl

import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple
import cn.nukkit.Player
import cn.nukkit.form.element.Element
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.response.FormResponseCustom
import cn.nukkit.form.response.FormResponseData
import cn.nukkit.form.window.FormWindow
import cn.nukkit.form.window.FormWindowModal
import kotlin.reflect.KMutableProperty1

internal val createPlayerMap = mutableMapOf<FormWindow, Player>()

// 缓存 player
var FormWindow.target: Player?
    get() = createPlayerMap[this]
    set(value) {
        createPlayerMap[this] = value!!
    }

var FormWindowModal.trueText: String
    get() = this.button1
    set(value) {
        this.button1 = value
    }

var FormWindowModal.falseText: String
    get() = this.button2
    set(value) {
        this.button2 = value
    }

inline fun FormSimple(receiver: Player, title: String, init: AdvancedFormWindowSimple.() -> Unit): AdvancedFormWindowSimple {
    return AdvancedFormWindowSimple(title).apply {
        init()
        target = receiver
        target?.showFormWindow(this)
    }
}

inline fun FormSimple(title: String, init: AdvancedFormWindowSimple.() -> Unit): AdvancedFormWindowSimple {
    return AdvancedFormWindowSimple(title).apply {
        init()
        target?.showFormWindow(this)
    }
}

inline fun FormSimple(receiver: Player, init: AdvancedFormWindowSimple.() -> Unit): AdvancedFormWindowSimple {
    return AdvancedFormWindowSimple("").apply {
        title = "FormSimple"
        init()
        target = receiver
        target?.showFormWindow(this)
    }
}

inline fun FormSimple(init: AdvancedFormWindowSimple.() -> Unit): AdvancedFormWindowSimple {
    return AdvancedFormWindowSimple("").apply {
        title = "FormSimple"
        init()
        target?.showFormWindow(this)
    }
}

class AdvancedFormWindowCustomAdapter<T>(val resp: T) : AdvancedFormWindowCustom("") {

    internal val dropdownRespBinders = mutableMapOf<Int, KMutableProperty1<T, FormResponseData>>()
    internal val stepSliderRespBinders = mutableMapOf<Int, KMutableProperty1<T, FormResponseData>>()
    internal val inputRespBinders = mutableMapOf<Int, KMutableProperty1<T, String>>()
    internal val toggleRespBinders = mutableMapOf<Int, KMutableProperty1<T, Boolean>>()
    internal val sliderRespBinders = mutableMapOf<Int, KMutableProperty1<T, Float>>()

    fun bindDropdown(element: Element, bind: KMutableProperty1<T, FormResponseData>) {
        addElement(element)
        val id = elements.size - 1
        dropdownRespBinders[id] = bind
    }

    fun bindStepSlider(element: Element, bind: KMutableProperty1<T, FormResponseData>) {
        addElement(element)
        val id = elements.size - 1
        stepSliderRespBinders[id] = bind
    }

    fun bindInput(element: Element, bind: KMutableProperty1<T, String>) {
        addElement(element)
        val id = elements.size - 1
        inputRespBinders[id] = bind
    }

    fun bindToggle(element: Element, bind: KMutableProperty1<T, Boolean>) {
        addElement(element)
        val id = elements.size - 1
        toggleRespBinders[id] = bind
    }

    fun bindSlider(element: Element, bind: KMutableProperty1<T, Float>) {
        addElement(element)
        val id = elements.size - 1
        sliderRespBinders[id] = bind
    }

}

inline fun <reified T : FormCustomResponseModel> FormCustom(init: AdvancedFormWindowCustomAdapter<T>.() -> Unit): AdvancedFormWindowCustomAdapter<T> {
    return AdvancedFormWindowCustomAdapter(T::class.java.newInstance()).apply {
        title = "FormCustom"
        init()
        target?.showFormWindow(this)
    }
}

inline fun FormModal(init: AdvancedFormWindowModal.() -> Unit): AdvancedFormWindowModal {
    return AdvancedFormWindowModal("", "", "", "").apply {
        title = "FormModal"
        init()
        target?.showFormWindow(this)
    }
}

inline fun FormModal(title: String, content: String, trueText: String, falseTest: String, init: AdvancedFormWindowModal.() -> Unit): AdvancedFormWindowModal {
    return AdvancedFormWindowModal(title, content, trueText, falseTest).apply {
        init()
        target?.showFormWindow(this)
    }
}

fun AdvancedFormWindowSimple.onButtonClick(click: ElementButton.() -> Unit) {
    val listener: (ElementButton, Player) -> Unit = { btn, _ ->
        click(btn)
    }
    this.onClicked(listener)
}

fun AdvancedFormWindowSimple.onClose(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClosed(listener)
}

fun AdvancedFormWindowModal.onTrue(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClickedTrue(listener)
}

fun AdvancedFormWindowModal.onFalse(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClickedFalse(listener)
}

fun AdvancedFormWindowModal.onClose(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClosed(listener)
}

inline fun AdvancedFormWindowCustom.icon(image: ElementButtonImageData.() -> Unit) {
    val icon = ElementButtonImageData("", "")
    image(icon)
    this.icon = icon
}

fun AdvancedFormWindowCustom.onClose(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClosed(listener)
}

fun <T> AdvancedFormWindowCustomAdapter<T>.onElementRespond(click: T.() -> Unit) {
    val listener : (FormResponseCustom, Player) -> Unit = { response, _ ->
        inputRespBinders.forEach { (id, binder) ->
            binder.set(this.resp, response.getInputResponse(id))
        }
        toggleRespBinders.forEach { (id, binder) ->
            binder.set(this.resp, response.getToggleResponse(id))
        }
        sliderRespBinders.forEach { (id, binder) ->
            binder.set(this.resp, response.getSliderResponse(id))
        }
        stepSliderRespBinders.forEach { (id, binder) ->
            binder.set(this.resp, response.getStepSliderResponse(id))
        }
        dropdownRespBinders.forEach { (id, binder) ->
            binder.set(this.resp, response.getDropdownResponse(id))
        }

        click(this.resp)
    }
    this.onResponded(listener)
}

interface FormCustomResponseModel