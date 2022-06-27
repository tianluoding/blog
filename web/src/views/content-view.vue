<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <div class="wangeditor" :innerHTML="valueHtml"></div>
        <div class="vote-div">
          <a-button type="primary" shape="round" :size="'large'" @click="vote">
            <template #icon>
              <LikeOutlined />
              &nbsp;点赞：{{ebook.voteCount}}
            </template>
          </a-button>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout-content>
</template>

<script lang="ts">
import "@wangeditor/editor/dist/css/style.css";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { message } from "ant-design-vue";


import axios from "axios";

export default {
  setup() {
    const route = useRoute();
    const valueHtml = ref({});

    // 请求文档内容
    const requestContent = () => {
      axios.get("/content/" + route.params.id).then(response => {
        const data = response.data;
        if (data.code == 1) {
          valueHtml.value = data.data.content;
        } else {
          message.error(data.msg);
        }
      });
      handleQuery();
    };

    /**
     * 数据查询
     **/
    const ebook:any = ref({});
    const handleQuery = () => {
      axios.get("/ebook/" + route.params.id).then(response => {
        const data = response.data;
        if (data.code == 1) {
          ebook.value = data.data;
        } else {
          message.error(data.msg);
        }
      });
    };

    const vote = () => {
      axios.get("/ebook/vote/" + route.params.id).then(response => {
        const data = response.data;
        if (data.code == 1) {
          message.success(data.data);
          ebook.value.voteCount = ebook.value.voteCount + 1;
        } else {
          message.error(data.msg);
        }
      })
    }

    onMounted(() => {
      requestContent();
    });

    return {
      valueHtml,
      requestContent,
      ebook,
      vote
    };
  }
};
</script>

<style>
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul,
ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.wangeditor img {
  max-width: 100%;
  height: auto;
}

/* 视频自适应 */
.wangeditor iframe {
  width: 100%;
  height: 400px;
}
</style>