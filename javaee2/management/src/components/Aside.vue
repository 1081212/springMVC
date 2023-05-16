<template>
  <div>
        <el-menu
            style="width: 250px;min-height: calc(100vh - 100px);font-weight: bolder"
            default-active="home"
            router
            class="el-menu-vertical-demo">
<!--            @open="handleOpen"-->
<!--            @close="handleClose"-->
          <el-menu-item index="home" :rout="{path:'/write'}">我的主页</el-menu-item>
        <el-submenu index="1">
          <template #title>模型预测</template>
          <el-menu-item index="write" :rout="{path:'/write'}">单样本预测</el-menu-item>
          <el-menu-item index="sign" :rout="{path:'/sign'}">数据集预测</el-menu-item>
        </el-submenu>
          <el-menu-item index="datas" :rout="{path:'/write'}">预测历史</el-menu-item>
          <el-menu-item index="model" :rout="{path:'/write'}">模型查看</el-menu-item>
          <el-menu-item index="center" :rout="{path:'/write'}">个人中心</el-menu-item>

        </el-menu>


  </div>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      user:{},
    }
  },

  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
  },
}
</script>

<style scoped>

</style>