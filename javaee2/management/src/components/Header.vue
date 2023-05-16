<template>
  <div class="bgBackground" style="margin-top: 10px;height:  70px;line-height: 50px ;border-bottom: 15px solid #409EFF;display: flex ">
<!--    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: royalblue">合同操作系统</div>-->
    <el-image style="margin-left: 600px" :src="require('../assets/header.png')" fit="contain"></el-image>
    <div style="flex: 1"></div>
    <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" style="margin-right: 20px"></el-avatar>
    <div style="width: 100px"><el-dropdown>
      <el-button type="primary" style="margin-right: 130px">
        {{ user.username }}<i class="el-icon-arrow-down el-icon--right"></i>
      </el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>个人信息</el-dropdown-item>
        <el-dropdown-item @click.native="$router.push('/login')" >退出系统</el-dropdown-item>

      </el-dropdown-menu>
    </el-dropdown>
    </div>

  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return{
      user:{},
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
  }
}
</script>

<style scoped>

</style>