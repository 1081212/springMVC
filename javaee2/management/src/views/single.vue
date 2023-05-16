<template>
  <div class="body">
    <div style="padding: 30px;background:#409EFF;border-radius: 15px 15px 15px 15px ;
  box-shadow: 2px 2px 10px #475669;" >
      <p style="font-size: 120%;font-weight: bolder;color: white">
        单样本预测
      </p>
    </div>

    <div v-if="form.isdataset=='0'">
    <div class="box1">
      <el-form class="box1" ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
        <el-form-item label="模型选择" >
          <el-select v-model="form.model" placeholder="请选择">
            <el-option label="逻辑回归" value="logistic"></el-option>
            <el-option label="支撑向量机" value="svm"></el-option>
            <el-option label="朴素贝叶斯" value="beysi"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="特征指标选择" >
          <el-select v-model="form.isdataset" placeholder="请选择">
            <el-option label="cvs" value="0"></el-option>
            <el-option label="LDHH" value="1"></el-option>
            <el-option label="WchU" value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="NonTrivial
        Bugs">
          <el-input v-model="form.numberOfNonTrivialBugsFoundUntil" style="width: 200px">
          </el-input>
        </el-form-item>

        <el-form-item label="WEntropy">
          <el-input v-model="form.cvsWEntropy" style="width: 200px">
          </el-input>
        </el-form-item>

        <el-form-item label="Entropy">
          <el-input v-model="form.cvsEntropy" style="width: 200px">
          </el-input>
        </el-form-item>

        <el-form-item label="CriticalBugs">
          <el-input v-model="form.numberOfCriticalBugsFoundUntil" style="width: 200px">
          </el-input>
        </el-form-item>

        <!--      <el-form-item label="上传附件">-->
        <!--        <el-upload-->
        <!--            class="upload-demo"-->
        <!--            action="https://jsonplaceholder.typicode.com/posts/"-->
        <!--            :on-preview="handlePreview"-->
        <!--            :on-remove="handleRemove"-->
        <!--            :before-remove="beforeRemove"-->
        <!--            multiple-->
        <!--            :limit="3"-->
        <!--            :on-exceed="handleExceed"-->
        <!--            :file-list="fileList">-->
        <!--          <el-button size="small" type="primary">点击上传</el-button>-->
        <!--          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
        <!--        </el-upload>-->
        <!--      </el-form-item>-->


        <el-form-item>
          <el-button type="primary" @click="onSubmit">预测</el-button>
          <el-button @click="">取消</el-button>
        </el-form-item>
      </el-form>
    </div><div class="box2" >
        <el-form class="box1" ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
          <el-form-item label="LogEntropy">
            <el-input v-model="form.cvsLogEntropy" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="HighPriorityBugs">
            <el-input v-model="form.numberOfHighPriorityBugsFoundUntil" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="MajorBugs">
            <el-input v-model="form.numberOfMajorBugsFoundUntil" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="LinEntropy">
            <el-input v-model="form.cvsLinEntropy" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="BugsFound">
            <el-input v-model="form.numberOfBugsFoundUntil" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="ExpEntropy">
            <el-input v-model="form.cvsExpEntropy" style="width: 200px">
            </el-input>
          </el-form-item>
        </el-form>
        <el-dialog
            title="预测结果"
            style="font-weight: bolder"
            :visible.sync="dialogVisible"
            width="30%">
          <el-image v-if="this.result==='牛,没Bug..'"  class="a" style="margin-left: 40%;height: 100px" :src="require('../assets/cross.png')" fit="contain"></el-image>
          <el-image v-if="this.result==='恐怕是有点Bug..'"style="margin-left: 40%;height: 100px" :src="require('../assets/false.png')" fit="contain"></el-image>
          <div v-if="this.result==='牛,没Bug..'" style="margin-left:  40%;font-weight: bolder;font-size: 20px;color: darkgreen">{{ this.result }}</div>
          <div v-if="this.result==='恐怕是有点Bug..'"style="margin-left:  40%;font-weight: bolder;font-size: 20px;color: #FD6585">{{ this.result }}</div>
          <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">确定并保存</el-button>
  </span>
        </el-dialog>
      </div>
    </div>

    <div v-if="form.isdataset=='1'">
      <div class="box1">
        <el-form class="box1" ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
          <el-form-item label="模型选择" >
            <el-select v-model="form.model" placeholder="请选择">
              <el-option label="逻辑回归" value="logistic"></el-option>
              <el-option label="支撑向量机" value="svm"></el-option>
              <el-option label="朴素贝叶斯" value="beysi"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="特征指标选择" >
            <el-select v-model="form.isdataset" placeholder="请选择">
              <el-option label="cvs" value="0"></el-option>
              <el-option label="LDHH" value="1"></el-option>
              <el-option label="WchU" value="2"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="lcom">
            <el-input v-model="form.ldhhlcom" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Fanin">
            <el-input v-model="form.ldhhfanin" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Publicmethods">
            <el-input v-model="form.ldhhnumberofpublicmethods" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Private_attributes">
            <el-input v-model="form.ldhhnumberofprivateattributes" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Public_attributes">
            <el-input v-model="form.ldhhnumberofpublicattributes" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="private_methods">
            <el-input v-model="form.ldhhnumberofprivatemethods" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="attributes_inherited">
            <el-input v-model="form.ldhhnumberofattributesinherited" style="width: 200px">
            </el-input>
          </el-form-item>

          <!--      <el-form-item label="上传附件">-->
          <!--        <el-upload-->
          <!--            class="upload-demo"-->
          <!--            action="https://jsonplaceholder.typicode.com/posts/"-->
          <!--            :on-preview="handlePreview"-->
          <!--            :on-remove="handleRemove"-->
          <!--            :before-remove="beforeRemove"-->
          <!--            multiple-->
          <!--            :limit="3"-->
          <!--            :on-exceed="handleExceed"-->
          <!--            :file-list="fileList">-->
          <!--          <el-button size="small" type="primary">点击上传</el-button>-->
          <!--          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
          <!--        </el-upload>-->
          <!--      </el-form-item>-->


          <el-form-item>
            <el-button type="primary" @click="onSubmit">预测</el-button>
            <el-button @click="">取消</el-button>
          </el-form-item>
        </el-form>
      </div><div class="box2" >
      <el-form class="box1" ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
        <el-form-item label="Noc">
          <el-input v-model="form.ldhhnoc" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Wmc">
          <el-input v-model="form.ldhhwmc" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="attributes">
          <el-input v-model="form.ldhhnumberofattributes" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Linesofcode">
          <el-input v-model="form.ldhhnumberoflinesofcode" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Dit">
          <el-input v-model="form.ldhhdit" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Fanout">
          <el-input v-model="form.ldhhfanout" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="methods_inherited">
          <el-input v-model="form.ldhhnumberofmethodsinherited" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Rfc">
          <el-input v-model="form.ldhhrfc" style="width: 200px">
          </el-input>
        </el-form-item>
          <el-form-item label="Cbo">
            <el-input v-model="form.ldhhcbo" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="Numberofmethods">
            <el-input v-model="form.ldhhnumberofmethods" style="width: 200px">
            </el-input>
          </el-form-item>
      </el-form>
      <el-dialog
          title="预测结果"
          style="font-weight: bolder"
          :visible.sync="dialogVisible"
          width="30%">
        <el-image v-if="this.result==='牛,没Bug..'"  class="a" style="margin-left: 40%;height: 100px" :src="require('../assets/cross.png')" fit="contain"></el-image>
        <el-image v-if="this.result==='恐怕是有点Bug..'"style="margin-left: 20px;height: 50px" :src="require('../assets/false.png')" fit="contain"></el-image>
        <div v-if="this.result==='牛,没Bug..'" style="margin-left:  40%;font-weight: bolder;font-size: 20px;color: darkgreen">{{ this.result }}</div>
        <div v-if="this.result==='恐怕是有点Bug..'"style="margin-left:  40%;font-weight: bolder;font-size: 20px;color: #FD6585">{{ this.result }}</div>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">确定并保存</el-button>
  </span>
      </el-dialog>
    </div>
    </div>

    <div v-if="form.isdataset=='2'">
      <div class="box1">
        <el-form class="box1" ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
          <el-form-item label="模型选择" >
            <el-select v-model="form.model" placeholder="请选择">
              <el-option label="逻辑回归" value="logistic"></el-option>
              <el-option label="支撑向量机" value="svm"></el-option>
              <el-option label="朴素贝叶斯" value="beysi"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="特征指标选择" >
            <el-select v-model="form.isdataset" placeholder="请选择">
              <el-option label="cvs" value="0"></el-option>
              <el-option label="LDHH" value="1"></el-option>
              <el-option label="WchU" value="2"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="Publicattributes">
            <el-input v-model="form.wchunumberofpublicattributes" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Attributes">
            <el-input v-model="form.wchunumberofattributes" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Fanin">
            <el-input v-model="form.wchufanin" style="width: 200px">
            </el-input>
          </el-form-item>

          <el-form-item label="Privatemethods">
            <el-input v-model="form.wchunumberofprivatemethods" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="Methods">
            <el-input v-model="form.wchunumberofmethods" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="Private_attributes">
            <el-input v-model="form.wchunumberofprivateattributes" style="width: 200px">
            </el-input>
          </el-form-item>
          <el-form-item label="Noc">
            <el-input v-model="form.wchunoc" style="width: 200px">
            </el-input>
          </el-form-item>

          <!--      <el-form-item label="上传附件">-->
          <!--        <el-upload-->
          <!--            class="upload-demo"-->
          <!--            action="https://jsonplaceholder.typicode.com/posts/"-->
          <!--            :on-preview="handlePreview"-->
          <!--            :on-remove="handleRemove"-->
          <!--            :before-remove="beforeRemove"-->
          <!--            multiple-->
          <!--            :limit="3"-->
          <!--            :on-exceed="handleExceed"-->
          <!--            :file-list="fileList">-->
          <!--          <el-button size="small" type="primary">点击上传</el-button>-->
          <!--          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
          <!--        </el-upload>-->
          <!--      </el-form-item>-->


          <el-form-item>
            <el-button type="primary" @click="onSubmit">预测</el-button>
            <el-button @click="">取消</el-button>
          </el-form-item>
        </el-form>
      </div><div class="box2" >
      <el-form class="box1" ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
        <el-form-item label="Wmc">
          <el-input v-model="form.wchuwmc" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Dit">
          <el-input v-model="form.wchudit" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="attributes_inherited">
          <el-input v-model="form.wchunumberofattributesinherited" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="fanout">
          <el-input v-model="form.wchufanout" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="lcom">
          <el-input v-model="form.wchulcom" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="RFC">
          <el-input v-model="form.wchurfc" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Publicmethods">
          <el-input v-model="form.wchunumberofpublicmethods" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Cbo">
          <el-input v-model="form.wchucbo" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Methods_inherited">
          <el-input v-model="form.wchunumberofmethodsinherited" style="width: 200px">
          </el-input>
        </el-form-item>
        <el-form-item label="Linesofcode">
          <el-input v-model="form.wchunumberoflinesofcode" style="width: 200px">
          </el-input>
        </el-form-item>
      </el-form>
      <el-dialog
          title="预测结果"
          style="font-weight: bolder"
          :visible.sync="dialogVisible"
          width="30%">
        <el-image v-if="this.result==='牛,没Bug..'"  class="a" style="margin-left: 40%;height: 100px" :src="require('../assets/cross.png')" fit="contain"></el-image>
        <el-image v-if="this.result==='恐怕是有点Bug..'"style="margin-left: 40%;height: 100px" :src="require('../assets/false.png')" fit="contain"></el-image>
        <div v-if="this.result==='牛,没Bug..'" style="margin-left:  40%;font-weight: bolder;font-size: 20px;color: darkgreen">{{ this.result }}</div>
        <div v-if="this.result==='恐怕是有点Bug..'"style="margin-left:  40%;font-weight: bolder;font-size: 20px;color: #FD6585">{{ this.result }}</div>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">确定并保存</el-button>
  </span>
      </el-dialog>
    </div>
    </div>



  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {

      dialogVisible: false,
      result:'',
      user:{},
      form: {
        isdataset:'0',
        userid:'',
        model:'logistic',
      },
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
  },
  methods: {

    onSubmit() {
      this.form.userid=this.user.userid;
      if(this.form.isdataset=='0'){
        if(
            this.form.numberOfNonTrivialBugsFoundUntil&&
            this.form.cvsWEntropy&&
            this.form.cvsEntropy&&
            this.form.numberOfCriticalBugsFoundUntil&&
            this.form.cvsLogEntropy&&
            this.form.numberOfHighPriorityBugsFoundUntil&&
            this.form.numberOfMajorBugsFoundUntil&&
            this.form.cvsLinEntropy&&
            this.form.numberOfBugsFoundUntil&&
            this.form.cvsExpEntropy
        )
        {//上传
          request.post("/api/predict/single",this.form).then(res =>{
            console.log(res.data)
            if(res.data=='buggy'){
              this.result='恐怕是有点Bug..';
              this.dialogVisible=true;
            }else{
              this.result='牛,没Bug..';
              this.dialogVisible=true;
            }
// 获取所有类名为card的元素
            let cards = document.getElementsByClassName("card")

// 设置定时器，每0.2秒调用依次showCard()函数
            let i = setInterval("showCard(cards)", 200)
            let index = 0
          })
         }
        else{
          this.$message({
            type:"error",
            message:"请输入完整特征",
          })
        }


      }
      if(this.form.isdataset=='1'){
        if(this.form.ldhhlcom&&
            this.form.ldhhfanin&&
            this.form.ldhhnumberofpublicmethods&&
            this.form.ldhhnumberofprivateattributes&&
            this.form.ldhhnumberofpublicattributes&&
            this.form.ldhhnumberofprivatemethods&&
            this.form.ldhhnumberofattributesinherited&&
            this.form.ldhhnoc&&
            this.form.ldhhwmc&&
            this.form.ldhhnumberofattributes&&
            this.form.ldhhnumberoflinesofcode&&
            this.form.ldhhdit&&
            this.form.ldhhfanout&&
            this.form.ldhhnumberofmethodsinherited&&
            this.form.ldhhrfc&&
            this.form.ldhhcbo&&
            this.form.ldhhnumberofmethods){
          //上传
          request.post("/api/ldhh/single",this.form).then(res =>{
            console.log(res.data)
            if(res.data=='buggy'){
              this.result='恐怕是有点Bug..';
              this.dialogVisible=true;
            }else{
              this.result='牛,没Bug..';
              this.dialogVisible=true;
            }
// 获取所有类名为card的元素
            let cards = document.getElementsByClassName("card")

// 设置定时器，每0.2秒调用依次showCard()函数
            let i = setInterval("showCard(cards)", 200)
            let index = 0
          })
        }else {
          this.$message({
            type:"error",
            message:"请输入完整特征",
          })
        }
      }
      if(this.form.isdataset=='2'){
        if(
            this.form.wchunumberofpublicattributes&&
            this.form.wchunumberofattributes&&
            this.form.wchufanin&&
            this.form.wchunumberofprivatemethods&&
            this.form.wchunumberofmethods&&
            this.form.wchunumberofprivateattributes&&
            this.form.wchunoc&&
            this.form.wchuwmc&&
            this.form.wchudit&&
            this.form.wchunumberofattributesinherited&&
            this.form.wchufanout&&
            this.form.wchulcom&&
            this.form.wchurfc&&
            this.form.wchunumberofpublicmethods&&
            this.form.wchucbo&&
            this.form.wchunumberofmethodsinherited&&
            this.form.wchunumberoflinesofcode
        )
        {//上传
          request.post("/api/wchu/single",this.form).then(res =>{
            console.log(res.data)
            if(res.data=='buggy'){
              this.result='恐怕是有点Bug..';
              this.dialogVisible=true;
            }else{
              this.result='牛,没Bug..';
              this.dialogVisible=true;
            }
// 获取所有类名为card的元素
            let cards = document.getElementsByClassName("card")

// 设置定时器，每0.2秒调用依次showCard()函数
            let i = setInterval("showCard(cards)", 200)
            let index = 0
          })
        }
        else{
          this.$message({
            type:"error",
            message:"请输入完整特征",
          })
        }
      }


    },
    showCard(cards) {
      cards[index].style.opacity = 1
      index++

      if (index == cards.length) {
        clearInterval(i)
        index = 0
      }
    }
  }
}
</script>

}
<style scoped>
.body{
  padding: 10px;height: auto;margin-right: 15px;
  border-radius: 25px 25px 25px 25px ;
  box-shadow: 2px 2px 10px #06C;
}

.box1{
  position: absolute;
  left: 20%  ;
  top:25%;
}

.box2{
  position: absolute;
  left: 50%  ;
  top:25%;
}


.card {
  /* 设置div形状和阴影 */
  width: 200px;
  height: 80px;
  border-radius: 10px;
  border: 1px solid lightgray;
  box-shadow: 0 4px 5px 0 rgba(0, 0, 0, 0.2);

  /* 居中并设置上边距 */
  margin: auto;
  margin-top: 15px;

  /* 文字水平和垂直居中 */
  display: flex;
  align-items: center;
  justify-content: center;

  /* 默认隐藏，显示过渡需0.5s */
  opacity: 0;
  transition: opacity 0.5s;
}

</style>