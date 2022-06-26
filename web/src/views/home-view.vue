<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
          :openKeys="openKeys"
        >
          <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template v-slot:title>
              <span>
                <user-outlined />
                {{item.name}}
              </span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <MailOutlined />
              <span>{{child.name}}</span>
            </a-menu-item>
          </a-sub-menu>
          <a-menu-item key="tip" :disabled="true">
            <span>以上菜单在分类管理配置</span>
          </a-menu-item>
        </a-menu>
      </a-layout-sider>
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <div class="welcome" v-show="isShowWelcome">
          <the-welcome></the-welcome>
        </div>
        <a-list
          v-show="!isShowWelcome"
          item-layout="vertical"
          size="large"
          :grid="{ gutter: 20, column: 3 }"
          :data-source="ebooks"
        >
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
                <span>
                  <component v-bind:is="'FileOutlined'" style="margin-right: 8px" />
                  {{ item.docCount }}
                </span>
                <span>
                  <component v-bind:is="'UserOutlined'" style="margin-right: 8px" />
                  {{ item.viewCount }}
                </span>
                <span>
                  <component v-bind:is="'LikeOutlined'" style="margin-right: 8px" />
                  {{ item.voteCount }}
                </span>
              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <router-link :to="'/content/' + item.id">{{ item.name }}</router-link>
                </template>
                <template #avatar>
                  <a-avatar :src="item.cover" />
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-layout-content>
    </a-layout>
  </a-layout-content>
</template>

<script lang="ts">
import {
  StarOutlined,
  LikeOutlined,
  MessageOutlined
} from "@ant-design/icons-vue";
import { defineComponent, onMounted, ref } from "vue";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";
import axios from "axios";
import TheWelcome from "@/components/the-welcome.vue";

export default defineComponent({
  name: "HomeView",
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    TheWelcome
  },
  setup() {
    console.log("setup");
    const ebooks = ref();

    const openKeys = ref();

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 100
    };
    const actions: Record<string, string>[] = [
      { type: "StarOutlined", text: "156" },
      { type: "LikeOutlined", text: "156" },
      { type: "MessageOutlined", text: "2" }
    ];

    onMounted(() => {
      handleQueryCategory();
    });

    const level1 = ref();
    level1.value = [];
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/list").then(response => {
        const data = response.data;
        if (data.code == 1) {
          categorys = data.data;
          console.log("原始数组：", categorys);

          // 加载完分类后，将侧边栏全部展开
          openKeys.value = [];
          for (let i = 0; i < categorys.length; i++) {
            openKeys.value.push(categorys[i].id);
          }

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);
          
        } else {
          message.error(data.message);
        }
      });
    };
    const isShowWelcome = ref(true);
    let categoryId2 = 0;
    const handleQueryEbook = () => {
      axios
        .get("/ebook/list", {
          params: {
            page: 1,
            pageSize: 1000,
            categoryId2: categoryId2
          }
        })
        .then(response => {
          const data = response.data;
          ebooks.value = data.data;
          // ebooks1.books = data.content;
        });
    };

    const handleClick = (value: any) => {
      // console.log("menu click", value)
      if (value.key === "welcome") {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
      // isShowWelcome.value = value.key === 'welcome';
    };

    return {
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions,
      ebooks,

      level1,
      handleClick,
      isShowWelcome,
      openKeys
    };
  }
});
</script>
