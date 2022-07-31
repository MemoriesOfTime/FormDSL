# FormDSL Module

> FormDSL 支持 kotlin 开发者使用 DSL 的方式来便捷的创建 Form 表单

## 前置

- GameCore
- FullKotlinLib

## 例子

1. 创建一个 Simple 类型的 Form 表单并发送给 `player`
```kotlin
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
```

2. 创建一个 Custom 类型的 Form 表单
   - 需要传入构造一个返回数据类型的类
   - 每添加一个控件需要绑定其返回数据类型到你构造的类的字段上( label 控件除外)

```kotlin
class MyResp : FormCustomResponseModel {
    lateinit var inputResp: String
    lateinit var dropdownResp: FormResponseData
    var toggleResp: Boolean = false
    var sliderResp: Float = 1F
    lateinit var stepSliderResp: FormResponseData
}

...

FormCustom<MyResp> {  // 传入构造的类的泛型即可
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
        option {
           - "A"
           - "B"
           + "C"  // 默认
           - "D"
        }
    }

    onElementRespond {
        // 这里就可以快乐地使用构造的返回类型的字段了！
        player.sendMessage("you input $inputResp")
        player.sendMessage("you toggle select to $toggleResp")
        // ...
    }

    onClose {
        player.sendMessage("You closed.")
    }
}
```

3. 创建一个 Modal 类型的 Form 表单

```kotlin
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
```

## 测试

- 将插件放入 `/plugins` 文件夹下加载
- OP 状态下指令 `/formtest [1|2|3]` 即可测试 