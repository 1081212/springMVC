
<template style="background-color: royalblue" >

  <div class="body">
    <div><p style="font-size: 200%;color: #409EFF;font-weight: bolder;margin-top: 50px;margin-left: 50px">
      基于机器学习的缺陷预测系统
    </p>
      <p style="font-size: 100%;color: darkgray;font-weight: bolder;margin-left: 50px;margin-top: 20px">
        使用我们的系统预测Bug，就是您最大的Bug。
      </p></div>
    <div class="hello">
      <p style="font-size: 20px;font-weight: bolder">用户"{{ user.username }}"您好，今天也是元气满满的一天~</p>
    </div>

<!--    <div>-->
<!--      <el-image style="margin-left: 60px;height: 400px;margin-top:80px " :src="require('../assets/back1.png')" ></el-image>-->
<!--    </div>-->

    <div class="block"style="background-color:  black;height: 305px">
      <el-carousel height="300px" >
        <el-carousel-item v-for="(item, index) in urls" :key="index">
          <img v-bind:src="item.url" style="width: 100%;height: 100%">
        </el-carousel-item>
      </el-carousel>
    </div>


    <div class="div_abs">
      <el-button type="primary" icon="el-icon-document" round class="bt_main"style="margin-left: 10px;font-size: 150%;font-weight: bolder" @click="$router.push('/write')">单样本预测</el-button>
      <el-button  type="danger" icon="el-icon-document-copy" round class="bt_main" style="margin: 10px;font-size: 150%;font-weight: bolder" @click="$router.push('/sign')">数据集预测</el-button>
      <el-button  type="success" icon="el-icon-coordinate" round class="bt_main"  style="font-size: 150%;font-weight: bolder"@click="$router.push('/model')" >模型展示</el-button>
      <el-button  type="warning" icon="el-icon-refresh-left" round class="bt_main" style="font-size: 150%;font-weight: bolder" @click="$router.push('/datas')" >预测记录</el-button>
    </div>
  </div>


</template>
<script type="text/javascript">

import request from "@/utils/request";

export default {
  created() {
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
    // request.get("/api/datasets_center/deleteHistory",).then(res =>{
    // })
    // this.$message({
    // })
  },
  data() {
    return {
      urls:[
        {url:require('../assets/svm.jpg')},
        {url:require('../assets/logic.jpg')},

      ],
      map: null,
    }
  },
  mounted() {
  },
  methods: {}
}
</script>
<style>
.block{
  width: 500px;
  position: absolute;
  left: 20%;
  top:40%;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}


.div_abs{
  width:700px;
  height:120px;
  right:0%;
  top:35%;
  float:right;
  position:absolute;
}
.bt_main{
  box-shadow: 3px 3px 3px #888888;
  height: 200px;
  width:300px;
}

.body{
  padding: 10px;height: auto;margin-right: 15px;
  border-radius: 25px 25px 25px 25px ;
  box-shadow: 2px 2px 10px #06C;
}

.hello {
  position: absolute;
  top:15%;
  right: 5%;
  width: 600px;
  border-radius: 15px;
  background-color: #1FC7ED;
  height: 110px;
  margin: 20px 0 20px 0;
  color: white;
  padding: 28px 0 0 100px;
}
</style>