<template>
  <div>
    <button class="operate-button" @click="handleClick">
      <img :src="resolve_img_url(icon_src)" alt />
      <p>{{title}}</p>
    </button>
    <el-dialog title="修改信息" :visible.sync="dialogFormVisible">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="ruleForm.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="number">
          <el-input v-model="ruleForm.number"></el-input>
        </el-form-item>
        <el-form-item label="工作" prop="idNumber">
          <el-input v-model="ruleForm.job"></el-input>
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
      rules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        number: [{ required: true, message: "请输入手机号", trigger: "blur" }],
        sex: [{ required: true, message: "请输入性别", trigger: "blur" }],
        job: [
          { required: true, message: "请输入工作", trigger: "blur" },
        ],
      },
      dialogFormVisible: false,
      ruleForm: {
        name: "",
        number: "",
        sex: "",
        job: "",
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
            .get("/api/user/alterInfo", {
              params: {
                userid: this.user.userid,
                name: this.ruleForm.name,
                number: this.ruleForm.number,
                sex: this.ruleForm.sex,
                job: this.ruleForm.job,
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
