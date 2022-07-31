package cn.lanink.formdsl

import cn.lanink.formdsl.dsl.*
import cn.nukkit.Player
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.response.FormResponseData

fun exampleSimple(player: Player) {
    FormSimple {
        target = player  // 设置了 player 将会自动发送给 player，可以省略手动 showToPlayer
        title = "This is Title"
        Button {
            text = "Button1"
            icon {  // 声明 Button 时设置了 icon，lambda 表达式里的 image { } 不会覆盖
                type = ElementButtonImageData.IMAGE_DATA_TYPE_PATH
                data = "textures/blocks/bedrock.png"
            }
            onPlayerClick {
                sendMessage("You click button1")
            }
        }
        Button("Button2") {
            onPlayerClick {
                sendMessage("You click button2")
            }
        }

        onButtonClick {
            player.sendMessage("You click $text")
        }

        onClose {
            player.sendMessage("You closed")
        }
    }
}

class MyResp : FormCustomResponseModel {
    lateinit var inputResp: String
    lateinit var dropdownResp: FormResponseData
    var toggleResp: Boolean = false
    var sliderResp: Float = 1F
    lateinit var stepSliderResp: FormResponseData
}

fun exampleCustom(player: Player) {

    FormCustom<MyResp> {
        target = player
        title = "This is Title"

        icon {
            type = ElementButtonImageData.IMAGE_DATA_TYPE_PATH
            data = "textures/blocks/bedrock.png"
        }

        Label("This is a Label")

        // 声明时需要绑定传入的RespModel一个合适的字段
        Input(MyResp::inputResp) {
            text = "Input1"
            placeHolder = "input your name"
            default = "iGxnon"
        }

        Dropdown(MyResp::dropdownResp) {
            text = "Dropdown1"
            option("A", "B" to true, "C")  // B 是默认值
        }

        Toggle(MyResp::toggleResp) {
            text = "is Male?"
            default = true
        }

        Slider(MyResp::sliderResp) {
            text = "Slider1"
            min = 1F
            max = 15F
            step = 2
            default = 5F
        }

        StepSlider(MyResp::stepSliderResp) {
            text = "StepSlider1"
            step("A", "B", "C" to true)  // C 是默认值
        }

        onElementRespond {
            player.sendMessage("you input $inputResp")
            player.sendMessage("you toggle select to $toggleResp")
            // ...
        }

        onClose {
            player.sendMessage("You closed.")
        }
    }
}

fun exampleModal(player: Player) {
    FormModal {
        target = player
        title = "This is Title"
        content = "This is content"
        trueText = "True"
        falseText = "False"

        onTrue {
            player.sendMessage("Ture!")
        }

        onFalse {
            player.sendMessage("False!")
        }

        onClose {
            player.sendMessage("Close!")
        }
    }
}