<template>
  <a-layout>
    <a-layout-content :style="{background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}">
      <a-modal
        v-model:visible="visible"
        title="Title"
        :confirm-loading="confirmLoading"
        @ok="handleOk"
      >
        <a-form :model="ebook" :label-col="{span: 5}">
          <a-form-item label="封面">
            <a-input v-model:value="ebook.cover" />
          </a-form-item>
          <a-form-item label="名称">
            <a-input v-model:value="ebook.name" />
          </a-form-item>
          <a-form-item label="分类">
            <a-cascader
              v-model:value="categoryIds"
              :field-names="{ label: 'name', value: 'id', children: 'children' }"
              :options="level1"
            />
          </a-form-item>
          <a-form-item label="描述">
            <a-input v-model:value="ebook.description" />
          </a-form-item>
        </a-form>
      </a-modal>

      <a-form layout="inline">
        <a-form-item>
          <a-input v-model:value="name" placeholder="search">
            <template #prefix>
              <UserOutlined style="color: rgba(0, 0, 0, 0.25)" />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" @click="handleSearch(pagination, name)">搜索</a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">新增</a-button>
        </a-form-item>
      </a-form>
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
              <smile-outlined />名称
            </span>
          </template>
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <a-avatar :src="record.cover" />
          </template>
          <template v-if="column.key === 'name'">
            <a>{{ record.name }}</a>
          </template>
          <template v-if="column.key === 'category'">
            <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
          </template>
          <template v-if="column.key === 'description'">{{ record.description }}</template>
          <template v-else-if="column.key === 'action'">
            <span>
              <a-button type="primary">
                <router-link to="/editor">文本编辑</router-link>
              </a-button>
              <a-divider type="vertical" />
              <a-button type="primary" @click="edit(record)">编辑</a-button>
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
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

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
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: "封面",
        dataIndex: "cover",
        key: "cover"
      },
      {
        title: "名称",
        dataIndex: "name",
        key: "name"
      },
      {
        title: "分类",
        dataIndex: "category",
        key: "category"
      },
      {
        title: "描述",
        dataIndex: "description",
        key: "description"
      },
      {
        title: "Action",
        key: "action"
      }
    ];

    onMounted(() => {
      handleQueryCategory();
    });
    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios
        .get("/ebook/page", {
          params: {
            page: params.page,
            pageSize: params.pageSize,
            name: params.name
          }
        })
        .then(response => {
          loading.value = false;
          const data = response.data;
          if (data.code == 1) {
            ebooks.value = data.data.records;

            pagination.value.current = params.page;
            pagination.value.total = data.data.total;
            console.log(pagination.value.total);
          } else {
            message.error(data.msg);
          }
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
    /**
     * 按名称模糊搜索
     */
    const handleSearch = (pagination: any, name: any) => {
      handleQuery({
        page: pagination.current,
        pageSize: pagination.pageSize,
        name: name
      });
    };

    const categoryIds = ref();
    const ebook: any = ref({});
    const modalText = ref<string>("Content of the modal");
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    const handleOk = () => {
      confirmLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then(response => {
        const data = response.data;
        if (data.code == 1) {
          visible.value = false;
          confirmLoading.value = false;

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            pageSize: pagination.value.pageSize
          });
        } else {
          message.error(data.msg);
        }
      });
    };

    const handleDeleteOk = (id: string) => {
      axios.delete("/ebook/delete/" + id).then(response => {
        const data = response.data;
        if (data.code == 1) {
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            pageSize: pagination.value.pageSize
          });
        }
      });
    };
    /**
     * 表单
     */

    const edit = (record: any) => {
      visible.value = true;
      ebook.value = record;
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };
    const add = () => {
      visible.value = true;
      ebook.value = {};
    };

    const level1 = ref();
    level1.value = [];
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/list").then(response => {
        loading.value = false;
        const data = response.data;
        if (data.code == 1) {
          categorys = data.data;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: pagination.value.current,
            pageSize: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    return {
      modalText,
      visible,
      confirmLoading,
      edit,
      add,
      ebook,
      name,
      handleOk,
      ebooks,

      handleDeleteOk,

      pagination,
      columns,
      loading,
      handleTableChange,
      handleSearch,

      level1,
      categorys,
      categoryIds,

      getCategoryName
    };
  }
});
</script>
