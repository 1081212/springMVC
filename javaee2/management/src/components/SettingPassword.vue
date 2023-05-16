<template>
  <div>
    <button class="operate-button" @click="handleClick">
      <img :src="resolve_img_url(icon_src)" alt />
      <p>{{title}}</p>
    </button>
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input :type="[flag?'text':'password']" v-model="ruleForm.oldPassword">
            <i
              slot="suffix"
              :class="[flag?'el-icon-minus':'el-icon-view']"
              style="margin-top:8px;font-size:18px;"
              autocomplete="auto"
              @click="flag=!flag"
            />
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input :type="[flag1?'text':'password']" v-model="ruleForm.newPassword">
            <i
              slot="suffix"
              :class="[flag1?'el-icon-minus':'el-icon-view']"
              style="margin-top:8px;font-size:18px;"
              autocomplete="auto"
              @click="flag1=!flag1"
            />
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmUpdate('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "SettingButton",
  props: {
    icon_src: String,
    title: String,
  },
  data() {
    return {
      flag: false,
      flag1: false,
      rules: {
        oldPassword: [
          { required: true, message: "请输入旧邮箱地址", trigger: "blur" },
        ],
        newPassword: [
          { required: true, message: "请输入新邮箱地址", trigger: "blur" },
        ],
      },
      dialogFormVisible: false,
      ruleForm: {
        oldPassword: "",
        newPassword: "",
      },
      formLabelWidth: "120px",
    };
  },
  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
  },
  methods: {
    resolve_img_url: function (path) {
      let images = require.context("../assets/", false, /\.png$|\.jpg$/);
      return images("./" + path);
    },
    handleClick() {
      this.dialogFormVisible = true;
    },
    confirmUpdate(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 校验通过
          request
            .get("/api/user/alterPassword", {
              params: {
                userid: this.user.userid,
                oldPassword: this.ruleForm.oldPassword,
                newPassword: this.ruleForm.newPassword,
              },
            })
            .then((res) => {
              if (res.code === "0") {
                console.log(res.data),
                this.dialogFormVisible = false,
                this.$message({
                  type:"success",
                  message:"修改成功",
                })
              } else {
                console.log(res),
                  this.$message({
                    type: "error",
                    message: res.msg,
                  });
              }
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
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
