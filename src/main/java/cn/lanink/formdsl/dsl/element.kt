@file:Suppress("UNUSED")
package cn.lanink.formdsl.dsl

import cn.lanink.gamecore.form.element.ResponseElementButton
import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple
import cn.nukkit.Player
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.element.ElementDropdown
import cn.nukkit.form.element.ElementInput
import cn.nukkit.form.element.ElementLabel
import cn.nukkit.form.element.ElementSlider
import cn.nukkit.form.element.ElementStepSlider
import cn.nukkit.form.element.ElementToggle
import cn.nukkit.form.response.FormResponseData
import kotlin.reflect.KMutableProperty1


inline fun AdvancedFormWindowSimple.Button(init: (@ElementDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton("Button").apply(init))
}

inline fun AdvancedFormWindowSimple.Button(icon: ElementButtonImageData, init: (@ElementDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton("Button", icon).apply(init))
}

inline fun AdvancedFormWindowSimple.Button(text: String, init: (@ElementDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton(text).apply(init))
}

inline fun AdvancedFormWindowSimple.Button(text: String, icon: ElementButtonImageData, init: (@ElementDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton(text, icon).apply(init))
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Dropdown(bind: KMutableProperty1<T, FormResponseData>, init: (@ElementDslMarker ElementDropdown).() -> Unit) {
    this.bindDropdown(ElementDropdown("").apply(init), bind)
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Input(bind: KMutableProperty1<T, String>, init: (@ElementDslMarker ElementInput).() -> Unit) {
    this.bindInput(ElementInput("").apply(init), bind)
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Toggle(bind: KMutableProperty1<T, Boolean>, init: (@ElementDslMarker ElementToggle).() -> Unit) {
    this.bindToggle(ElementToggle("").apply(init), bind)
}

fun AdvancedFormWindowCustom.Label(text: String) {
    this.addElement(ElementLabel(text))
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Slider(bind: KMutableProperty1<T, Float>, init: (@ElementDslMarker ElementSlider).() -> Unit) {
    this.bindSlider(ElementSlider("", 0F, 10F, 1).apply(init), bind)
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.StepSlider(bind: KMutableProperty1<T, FormResponseData>, init: (@ElementDslMarker ElementStepSlider).() -> Unit) {
    this.bindStepSlider(ElementStepSlider("").apply(init), bind)
}

var ElementToggle.default: Boolean
    get() = this.isDefaultValue
    set(value) {
        this.isDefaultValue = value
    }

var ElementInput.default: String
    get() = this.defaultText
    set(value) {
        this.defaultText = value
    }

var ElementSlider.default: Float
    get() = this.defaultValue
    set(value) {
        this.defaultValue = value
    }


fun ResponseElementButton.onPlayerClick(click: (@PlayerDslMarker Player).() -> Unit) {
    this.onClicked(click)
}

inline fun ElementButton.icon(init: ElementButtonImageData.() -> Unit) {
    val icon = ElementButtonImageData("", "")
    init(icon)
    this.addImage(icon)
}

fun <T> ElementDropdown.option(vararg options: T) {
    for (option in options) {
        if (option is String) {
            this.addOption(option)
        }
        if (option is Pair<*, *>) {
            this.addOption(option.first as String, option.second as Boolean)
        }
    }
}

fun <T> ElementStepSlider.step(vararg steps: T) {
    for (step in steps) {
        if (step is String) {
            this.addStep(step, false)
        }
        if (step is Pair<*, *>) {
            this.addStep(step.first as String, step.second as Boolean)
        }
    }
}