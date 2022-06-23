<template>
  <a-layout>
    <a-layout-content :style="{background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}">
      <a-table
        :columns="columns"
        :data-source="ebooks"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template #headerCell="{ column }">
          <template v-if="column.key === 'name'">
            <span>
              <smile-outlined />Name
            </span>
          </template>
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'name'">
            <a>{{ record.name }}</a>
          </template>
          <template v-if="column.key === 'categoryId1'">
            <a>{{ record.categoryId1 }}</a>
          </template>
          <template v-if="column.key === 'categoryId2'">
            <a>{{ record.categoryId2 }}</a>
          </template>
          <template v-if="column.key === 'description'">{{ record.description }}</template>
          <template v-else-if="column.key === 'tags'">
            <span>
              <a-tag
                v-for="tag in record.tags"
                :key="tag"
                :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
              >{{ tag.toUpperCase() }}</a-tag>
            </span>
          </template>
          <template v-else-if="column.key === 'action'">
            <span>
              <a-button type="primary" @click="showModal">编辑</a-button>
              <a-modal
                v-model:visible="visible"
                title="Title"
                :confirm-loading="confirmLoading"
                @ok="handleOk"
              >
                <p>{{ modalText }}</p>
              </a-modal>
              <a-divider type="vertical" />
              <a-button type="danger">删除</a-button>
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

export default defineComponent({
  name: "AdminEbook",
  components: {
    SmileOutlined,
    DownOutlined
  },
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        name: "名称",
        dataIndex: "name",
        key: "name"
      },
      {
        title: "分类1",
        dataIndex: "categoryId1",
        key: "categoryId1"
      },
      {
        title: "分类2",
        dataIndex: "categoryId2",
        key: "categoryId2"
      },
      {
        title: "描述",
        dataIndex: "description",
        key: "description"
      },
      {
        title: "Tags",
        key: "tags",
        dataIndex: "tags"
      },
      {
        title: "Action",
        key: "action"
      }
    ];

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios
        .get("/ebook/page", {
          params: {
            page: params.page,
            pageSize: params.pageSize
          }
        })
        .then(response => {
          loading.value = false;
          const data = response.data;
          ebooks.value = data.data.records;

          pagination.value.current = params.page;
          pagination.value.total = data.data.total;
          console.log(pagination.value.total);
        });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("分页参数: " + pagination);
      handleQuery({
        page: pagination.current,
        pageSize: pagination.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        pageSize: pagination.value.pageSize
      });
    });

    const modalText = ref<string>('Content of the modal');
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    const showModal = () => {
      visible.value = true;
    };

    const handleOk = () => {
      modalText.value = 'The modal will be closed after two seconds';
      confirmLoading.value = true;
      setTimeout(() => {
        visible.value = false;
        confirmLoading.value = false;
      }, 2000);
    };

    return {
      modalText,
      visible,
      confirmLoading,
      showModal,
      handleOk,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
    };
  }
});
</script>
