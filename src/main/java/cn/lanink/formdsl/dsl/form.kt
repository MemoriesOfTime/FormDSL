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
import cn.nukkit.form.window.FormWindowModal
import kotlin.reflect.KMutableProperty1


// 改名字
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

@FormDslMarker
class AdvancedFormWindowSimpleAdapter(var target: Player? = null) : AdvancedFormWindowSimple("")

inline fun FormSimple(init: AdvancedFormWindowSimpleAdapter.() -> Unit): AdvancedFormWindowSimpleAdapter {
    return AdvancedFormWindowSimpleAdapter().apply {
        title = "FormSimple"
        init()
        target?.showFormWindow(this)
    }
}

@FormDslMarker
class AdvancedFormWindowCustomAdapter<T>(val resp: T, var target: Player? = null) : AdvancedFormWindowCustom("") {

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

@FormDslMarker
class AdvancedFormWindowModalAdapter(var target : Player? = null) : AdvancedFormWindowModal("", "", "", "")

inline fun FormModal(init: AdvancedFormWindowModalAdapter.() -> Unit): AdvancedFormWindowModalAdapter {
    return AdvancedFormWindowModalAdapter().apply {
        title = "FormModal"
        init()
        target?.showFormWindow(this)
    }
}

fun AdvancedFormWindowSimpleAdapter.onButtonClick(click: (@FormDslMarker ElementButton).() -> Unit) {
    val listener: (ElementButton, Player) -> Unit = { btn, _ ->
        click(btn)
    }
    this.onClicked(listener)
}

fun AdvancedFormWindowSimpleAdapter.onClose(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClosed(listener)
}

fun AdvancedFormWindowModalAdapter.onTrue(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClickedTrue(listener)
}

fun AdvancedFormWindowModalAdapter.onFalse(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClickedFalse(listener)
}

fun AdvancedFormWindowModalAdapter.onClose(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClosed(listener)
}

inline fun <T> AdvancedFormWindowCustomAdapter<T>.icon(image: (@FormDslMarker ElementButtonImageData).() -> Unit) {
    val icon = ElementButtonImageData("", "")
    image(icon)
    this.icon = icon
}

fun <T> AdvancedFormWindowCustomAdapter<T>.onClose(click: () -> Unit) {
    val listener: (Player) -> Unit = {
        click()
    }
    this.onClosed(listener)
}

fun <T> AdvancedFormWindowCustomAdapter<T>.onElementRespond(click: (@FormDslMarker T).() -> Unit) {
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