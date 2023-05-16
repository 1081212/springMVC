<template>
  <div class="wrapper" :style="{ backgroundImage: 'url(' + require('../assets/ship.png') + ')', backgroundSize: 'cover' }">
  <div style="margin: 200px auto; background-color: #efeaea!important; width: 350px; height: 350px; padding: 20px; border-radius: 10px">
      <div style="text-align: center">
        <el-image style="height: 70px;" :src="require('../assets/header.png')" fit="scale-down"></el-image>
      </div>

      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登 录</b></div>
    <div>
      <div v-if="showUsernameTip" style="position: absolute;z-index: 9999;top: -20px;">
        <el-alert :title="usernameTip" type="success"></el-alert>
      </div>
      <el-input placeholder="请输入账号" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username" @input="checkUsernameLength"></el-input>


      <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-aim" show-password v-model="user.password" ></el-input>
      <div v-if="showPasswordTip">{{ passwordTip }}</div>
    </div>
    <div style="margin: 10px 0;text-align: right">
        <el-button type="primary" size="small" autocomplete="off" @click="login" >登录</el-button>
        <el-button type="primary" size="small" autocomplete="off" @click="register">注册</el-button>
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
      user: {
        username: '',
        password: ''
      },
      showUsernameTip: false,
      usernameTip: 'Username must be at least 5 characters long'
    }
  },
  methods:{
    checkUsernameLength() {
      if (this.user.username.length < 5) {
        this.showUsernameTip = true;
      } else {
        this.showUsernameTip = false;
      }
    },
    register(){
      this.$router.replace('/register')
    } ,
    login(){
      request.post("/api/login", this.user,{header:{'Content-Type':'application/json;charset=utf-8'}},).then(res =>{
        if(res.code === '0'){
          console.log(res.data),
          console.log("success"),sessionStorage.setItem("user",JSON.stringify(res.data)),
          this.$router.push("/home"),

          this.$message({
            type:"success",
            message:"登陆成功",
          })}else{
            console.log(res),
            this.$message({
              type:"error",
              message:res.msg
          })
        }
      })
    }
  }
}
</script>

<style>
  .wrapper{
    height: 100vh;
    background-image: linear-gradient(to bottom right,#C2FFD8,#465EFB);
    overflow: hidden;
  }

</style>