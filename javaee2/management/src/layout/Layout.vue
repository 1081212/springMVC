<template>
  <div >
    <Header />
    <!--    主题-->
    <div style="display :flex">
      <!--      侧边栏-->
      <Aside style="margin-right: 10px"/>
      <!--      主体-->
      <router-view style="flex:1;margin-top: 15px;width: 80% "/>
    </div>
  </div>
</template>

<script>

import Header from "@/components/Header";
import Aside from "@/components/Aside";
export default {
  name: "Layout",
  components:{
    Header,
    Aside
  }

}
</script>

<style scoped>

</style>