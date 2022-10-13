<template>
  <div>
    <el-form :inline="true" :model="queryParam" class="demo-form-inline">
      <el-form-item label="用户名">
        <el-input v-model="queryParam.userName" placeholder="用户名" />
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="queryParam.nickName" placeholder="昵称" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Query</el-button>
      </el-form-item>
    </el-form>
    <div class="flex">
      <el-button
        type="primary"
        :icon="Plus"
        @click="centerDialogVisible = true"
      />
      <el-button type="primary" :icon="Share" />
      <el-button type="primary" :icon="Delete" />
      <el-button type="primary" :icon="Search">Search</el-button>
      <el-button type="primary">
        Upload<el-icon class="el-icon--right"><Upload /></el-icon>
      </el-button>
    </div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column label="UserName" width="300">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-icon><timer /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.userName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" width="300">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-icon><timer /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.email }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="nickName" width="400">
        <template #default="scope">
          <el-popover
            effect="light"
            trigger="hover"
            placement="top"
            width="auto"
          >
            <template #default>
              <div>name: {{ scope.row.nickName }}</div>
              <div>phone: {{ scope.row.phonenumber }}</div>
            </template>
            <template #reference>
              <el-tag>{{ scope.row.nickName }}</el-tag>
            </template>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
            >Edit</el-button
          >
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
            >Delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      :page-size="queryParam.size"
      @current-change="changePage"
    />
    <!--新增用户和修改用户-->
    <el-dialog
      v-model="centerDialogVisible"
      title="新增用户"
      width="30%"
      center
    >
      <el-form :model="userFrom" ref="userFromref" label-width="120px">
        <el-form-item label="用户名">
          <el-input v-model="userFrom.userName" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="userFrom.nickName" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="userFrom.password" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="userFrom.confirmPassword" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitFrom">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>


<script setup>
import { Timer } from "@element-plus/icons-vue";
import { ref } from "vue";
import { listuser, add } from "@/api/user";
import { onMounted } from "vue";
import { Delete, Plus, Search, Share, Upload } from "@element-plus/icons-vue";

const queryParam = ref({
  size: 2,
  page: 0,
  userName: "",
  nickName: "",
  confirmPassword: "",
});

let userFrom = ref({
  userId: null,
  userName: "",
  nickName: "",
  password: "",
});

let centerDialogVisible = ref(false);
const tableData = ref([]);
const total = ref(0);

const getlistUser = function () {
  listuser(queryParam.value).then((res) => {
    tableData.value = res.data.content;
    total.value = res.data.totalElements;
  });
};

onMounted(() => {
  getlistUser();
});
const changePage = async function (current) {
  queryParam.value.page = current - 1;
  getlistUser();
};

const onSubmit = function () {
  getlistUser();
};

const submitFrom = function () {
  delete userFrom.value.confirmPassword
  add(userFrom.value).then((res) => {
    centerDialogVisible = false;
  });
};
</script>



<style>
</style>