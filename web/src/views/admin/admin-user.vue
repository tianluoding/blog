<template>
  <a-layout>
    <a-layout-content :style="{background: '#fff', padding: '24px', margin: 0, minHeight: '280px'}">
      <a-modal
        v-model:visible="visible"
        title="设置"
        :confirm-loading="confirmLoading"
        @ok="handleOk"
      >
        <a-form :model="user" :label-col="{span: 3}">
          <a-form-item label="登录名">
            <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
          </a-form-item>
          <a-form-item label="名称">
            <a-input v-model:value="user.name" />
          </a-form-item>
          <a-form-item label="密码">
            <a-input v-model:value="user.password" type="password" />
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
          <a-button type="primary" html-type="submit">搜索</a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">新增</a-button>
        </a-form-item>
      </a-form>
      <a-table :columns="columns" :data-source="users" :loading="loading">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'loginName'">{{record.loginName}}</template>
          <template v-if="column.key === 'name'">{{ record.name }}</template>
          <template v-if="column.key === 'password'">{{ record.password }}</template>
          <template v-else-if="column.key === 'action'">
            <span>
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
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: "登录名",
        dataIndex: "loginName",
        key: "loginName"
      },
      {
        title: "名称",
        dataIndex: "name",
        key: "name"
      },
      {
        title: "密码",
        dataIndex: "password",
        key: "password"
      },
      {
        title: "Action",
        key: "action"
      }
    ];

    onMounted(() => {
      handleQuery({});
    });
    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/user/list").then(response => {
        loading.value = false;
        const data = response.data;
        if (data.code == 1) {
          users.value = data.data;
        } else {
          message.error(data.msg);
        }
      });
    };

    const user: any = ref({});
    const modalText = ref<string>("Content of the modal");
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    const handleOk = () => {
      confirmLoading.value = true;
      axios.post("/user/save", user.value).then(response => {
        const data = response.data;
        if (data.code == 1) {
          visible.value = false;
          confirmLoading.value = false;

          // 重新加载列表
          handleQuery({});
        } else {
          message.error(data.msg);
        }
      });
    };

    const handleDeleteOk = (id: string) => {
      axios.delete("/user/delete/" + id).then(response => {
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
      user.value = record;
    };
    const add = () => {
      visible.value = true;
      user.value = {};
    };

    return {
      modalText,
      visible,
      confirmLoading,
      edit,
      add,
      user,
      handleOk,
      users,

      handleDeleteOk,

      columns,
      loading
    };
  }
});
</script>
