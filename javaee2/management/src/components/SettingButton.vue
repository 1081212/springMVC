<template>
  <div>
    <button class="operate-button" @click="handleClick">
      <img :src="resolve_img_url(icon_src)" alt />
      <p>{{title}}</p>
    </button>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
      <span>确定退出登录？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmLogout">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SettingButton",
  props: {
    icon_src: String,
    title: String,
  },
  data() {
    return {
      dialogVisible: false,
    };
  },
  methods: {
    resolve_img_url: function (path) {
      let images = require.context("../assets/", false, /\.png$|\.jpg$/);
      return images("./" + path);
    },
    handleClick() {
      this.dialogVisible = true;
    },
    confirmLogout() {
      this.dialogVisible = false;
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.operate-button {
  background-color: white;
  display: flex;
  padding: 8px 50px 8px 50px;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.operate-button img {
  width: 58px;
  height: 58px;
}

.operate-button p {
  width: 90px;
  text-align: center;
  height: 58px;
  line-height: 58px;
  font-size: 18px;
  color: #303133;
  font-weight: 600;
}
</style>