<template>
    <div>
        <div style="border: 1px solid #ccc; margin-top: 10px;">
            <Toolbar
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
                style="border-bottom: 1px solid #ccc"
            />
            <Editor
                :defaultConfig="editorConfig"
                :mode="mode"
                v-model="valueHtml"
                style="height: 400px; overflow-y: hidden;"
                @onCreated="handleCreated"
                @onChange="handleChange"
                @onDestroyed="handleDestroyed"
                @onFocus="handleFocus"
                @onBlur="handleBlur"
                @customAlert="customAlert"
                @customPaste="customPaste"
            />
        </div>
        <div style="padding: 12px">
          <a-button style="float:right" type="primary" @click="saveContent">保存</a-button>
        </div>
    </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css'
import { onBeforeUnmount, ref, shallowRef, onMounted } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { useRoute } from 'vue-router'

import { message } from "ant-design-vue";

import axios from "axios";

export default {
  components: { Editor, Toolbar },
  setup() {
    //获取路由参数
    const route = useRoute();
    // 编辑器实例，必须用 shallowRef，重要！
    const editorRef = shallowRef()
    // 内容 HTML
    const valueHtml = ref()
   
    const toolbarConfig = {}
    const editorConfig = { placeholder: '请输入内容...' }
    // 组件销毁时，也及时销毁编辑器，重要！
    onBeforeUnmount(() => {
        const editor = editorRef.value
        if (editor == null) return
        editor.destroy()
    })
    // 编辑器回调函数
    const handleCreated = (editor) => {
      console.log('created', editor);
      editorRef.value = editor // 记录 editor 实例，重要！
    }
    const handleChange = (editor) => {
      console.log('change:', editor.getHtml());
    }
    const handleDestroyed = (editor) => {
      console.log('destroyed', editor)
    }
    const handleFocus = (editor) => {
        console.log('focus', editor)
    }
    const handleBlur = (editor) => {
        console.log('blur', editor)
    }
    const customAlert = (info, type) => {
        alert(`【自定义提示】${type} - ${info}`)
    }
    const customPaste = (editor, event, callback) => {
        console.log('ClipboardEvent 粘贴事件对象', event)
        // 自定义插入内容
        editor.insertText('xxx')
        // 返回值（注意，vue 事件的返回值，不能用 return）
        callback(false) // 返回 false ，阻止默认粘贴行为
        // callback(true) // 返回 true ，继续默认的粘贴行为
    }
    const insertText = () => {
        const editor = editorRef.value
        if (editor == null) return
        editor.insertText(valueHtml)
    }
    const printHtml = () => {
        const editor = editorRef.value
        if (editor == null) return
        console.log(editor.getHtml())
    }
    const disable = () => {
        const editor = editorRef.value
        if (editor == null) return
        editor.disable()
    }

    // 请求文档内容
    const requestContent = () => {
      axios
        .get("/content/" + route.params.id)
        .then(response => {
          const data = response.data;
          if (data.code == 1) {
            valueHtml.value = data.data.content;
          } else {
            message.error(data.msg);
          }
        });
    }

    const content = ref({});
    // 保存文档内容
    const saveContent = () => {
      const editor = editorRef.value;
      content.value.id = route.params.id;
      content.value.content = editor.getHtml();
      console.log(content.value);
      axios
        .post("/content", content.value)
        .then(response => {
          const data = response.data;
          if (data.code == 1) {
            message.success(data.data);
          } else {
            message.error(data.msg);
          }
        });
    }

    onMounted( () => {
      requestContent()
    });

    return {
      editorRef,
      mode: 'default',
      valueHtml,
      toolbarConfig,
      editorConfig,
      handleCreated,
      handleChange,
      handleDestroyed,
      handleFocus,
      handleBlur,
      customAlert,
      customPaste,
      insertText,
      printHtml,
      disable,

      requestContent,
      saveContent
    };
  }
}
</script>