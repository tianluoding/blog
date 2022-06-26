<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <div v-html="valueHtml">
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout-content>
</template>

<script lang="ts">
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
    };

    onMounted(() => {
      requestContent();
    });

    return {
      valueHtml,
      requestContent
    };
  }
};
</script>
