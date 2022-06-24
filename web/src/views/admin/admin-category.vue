<template>
  <a-layout>
    <a-layout-content :style="{background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}">
      <p>
        <a-button type="primary" @click="add()" size="large">新增</a-button>
      </p>
      <a-table
        v-if="level1.length > 0"
        :columns="columns"
        :data-source="level1"
        :row-key="record => record.id"
        :loading="loading"
        :pagination="false"
        :defaultExpandAllRows="false"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'name'">{{ record.name }}</template>
          <template v-if="column.key === 'parent'">{{record.parent}}</template>
          <template v-if="column.key === 'sort'">{{record.sort}}</template>

          <template v-else-if="column.key === 'action'">
            <span>
              <a-button type="primary" @click="edit(record)">编辑</a-button>
              <a-modal
                v-model:visible="visible"
                title="Title"
                :confirm-loading="confirmLoading"
                @ok="handleOk"
              >
                <a-form :model="ebook" :label-col="{span: 2}">
                  <a-form-item label="名称">
                    <a-input v-model:value="ebook.name"/>
                  </a-form-item>
                  <a-form-item label="父分类">
                    <a-select v-model:value="ebook.parent" ref="select">
                      <a-select-option :value="0">无</a-select-option>
                      <a-select-option
                        v-for="c in level1"
                        :key="c.id"
                        :value="c.id"
                        :disabled="ebook.id === c.id"
                      >{{c.name}}</a-select-option>
                    </a-select>
                  </a-form-item>
                  <a-form-item label="顺序">
                    <a-input v-model:value="ebook.sort"/>
                  </a-form-item>
                </a-form>
              </a-modal>
              <a-divider type="vertical" />
              <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDeleteOk(record.id)"
              >
                <a-button type="danger">删除</a-button>
              </a-popconfirm>
              <a-divider type="vertical" />
            </span>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { SmileOutlined, DownOutlined } from "@ant-design/icons-vue";
import { defineComponent, onMounted, ref } from "vue";

import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

export default defineComponent({
  name: "AdminCategory",
  components: {
    SmileOutlined,
    DownOutlined
  },
  setup() {
    const categories = ref();
    const loading = ref(false);

    const columns = [
      {
        title: "名称",
        dataIndex: "name",
        key: "name"
      },
      {
        title: "父分类",
        dataIndex: "parent",
        key: "parent"
      },
      {
        title: "顺序",
        dataIndex: "sort",
        key: "sort"
      },
      {
        title: "Action",
        key: "action"
      }
    ];

    /**
     * 数据查询
     */
    const level1 = ref();
    level1.value = [];
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/list").then(response => {
        loading.value = false;
        const data = response.data;
        if (data.code == 1) {
          categories.value = data.data;
          level1.value = [];
          level1.value = Tool.array2Tree(categories.value, 0);
        } else {
          message.error(data.msg);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    const modalText = ref<string>("Content of the modal");
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    const handleOk = () => {
      confirmLoading.value = true;
      axios.post("/category/save", ebook.value).then(response => {
        const data = response.data;
        if (data.code == 1) {
          visible.value = false;
          confirmLoading.value = false;

          // 重新加载列表
          handleQuery();
        }
      });
    };

    const handleDeleteOk = (id: string) => {
      axios.delete("/category/delete/" + id).then(response => {
        const data = response.data;
        if (data.code == 1) {
          // 重新加载列表
          handleQuery();
        }
      });
    };

    /**
     * 表单
     */
    const ebook = ref({});
    const edit = (record: any) => {
      visible.value = true;
      ebook.value = record;
    };
    const add = () => {
      visible.value = true;
      ebook.value = {};
    };

    return {
      modalText,
      visible,
      confirmLoading,
      edit,
      add,
      ebook,
      handleOk,

      handleDeleteOk,
      level1,
      columns,
      loading
    };
  }
});
</script>
