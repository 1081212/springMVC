

<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 450px; padding: 20px; border-radius: 10px">
      <el-image style="margin-left: 20px;height: 50px" :src="require('../assets/header.png')" fit="contain"></el-image>
      <div style="margin: 20px 0;text-align: center;font-size: 24px ;"><b>注册</b></div>
      <el-input placeholder="请输入账号" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
      <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-aim" show-password v-model="user.password"></el-input>
      <el-input placeholder="请输入邮箱" size="medium" style="margin: 10px 0;width: 200px;margin-right: 10px" prefix-icon="el-icon-message" v-model="user.email"></el-input>
      <el-button type="primary" size="small"  @click="sendemail"
                 :disabled="disabled=!show"
      >
        <span v-show="show">获取验证码</span>
        <span v-show="!show" class="count">{{count}} s后再试</span>
      </el-button>
      <el-input placeholder="请输入验证码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-postcard" show-password v-model="user.verifying"></el-input>


      <div style="margin: 10px 0;text-align: center">
        <el-button type="primary" size="medium" autocomplete="off" @click="login" >立即注册！</el-button>
        <el-button type="primary" size="smasll" autocomplete="off" @click= "back">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  created() {
    sessionStorage.removeItem("user")
  },
  data(){
    return{
      show: true,
      count: "", // 初始化次数
      timer: null,
      user:{}
    }
  },
  methods:{
    back(){
      this.$router.push('/login')
    },
    login(){
      request.post("/api/user/register", this.user,{header:{'Content-Type':'application/json;charset=utf-8'}},).then(res =>{
        if(res.code === '0'){
          console.log(res.data),
              this.$message({
                type:"success",
                message:"注册成功",
              })
          this.$router.push("/login")
        }else{
          console.log(res),
              this.$message({
                type:"error",
                message:res.msg
              })
        }
      })
    },
    sendemail(){
      console.log(this.user)
      request.post("/api/user/sendemail", this.user,{header:{'Content-Type':'application/json;charset=utf-8'}},).then(res =>{
        if(res.code === '0'){
          console.log(res.data),
              this.$message({
                type:"success",
                message:"发送成功",
              })}else{
          console.log(res),
              this.$message({
                type:"error",
                message:res.msg
              })
        }
      })
      const TIME_COUNT = 60; //更改倒计时时间
      if (!this.timer) {
        this.count = TIME_COUNT;
        this.show = false;
        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= TIME_COUNT) {
            this.count--;
          } else {
            this.show = true;
            clearInterval(this.timer); // 清除定时器
            this.timer = null;
          }
        }, 1000);
      }
    }
  }
}
</script>

<style>
.wrapper{
  height: 100vh;
  background-image: linear-gradient(to bottom right,#FFD3A5,#FD6585);
  overflow: hidden;
}

</style>