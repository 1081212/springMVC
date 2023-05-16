<template>
  <div class="body">
    <div style="padding: 30px;background:#409EFF;border-radius: 15px 15px 15px 15px ;
  box-shadow: 2px 2px 10px #475669;"   >
      <p style="font-size: 120%;font-weight: bolder;color: white">
        数据集预测
      </p>
    </div>

    <el-form ref="form" :model="form" label-width="130px" style="margin: 20px;font-weight: bold">
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

            <el-form-item label="上传附件">
              <el-upload
                  size="small"
                  class="upload-demo"
                  headers="Content-Type': 'multipart/form-data"
                  drag
                  :on-change="handleChange"
                  :file-list="fileList"
                  action='/api/predict/dataset'
                  :auto-upload='false'
                  :before-upload="beforeAvatarUpload"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template v-slot:tip>
                  <div class="el-upload-tip el-upload-tip--small" style="margin-bottom: 5px">
                  </div>
                </template>
              </el-upload>
            </el-form-item>

      <el-dialog title="数据预测详情" :visible.sync="visible1">
        <el-table
            v-loading="loading"
            element-loading-text="模型预测中..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :data="predictData"
            border
            style="width: 100%"
            max-height="600">
          <el-table-column
              fixed
              prop="dataid"
              label="数据标号"
              width="150">
          </el-table-column>
          <el-table-column
              prop="numberOfNonTrivialBugsFoundUntil"
              label="NonTrivialBugs"
              width="120">
          </el-table-column>
          <el-table-column
              prop="cvsWEntropy"
              label="WEntropy"
              width="120">
          </el-table-column>
          <el-table-column
              prop="cvsEntropy"
              label="Entropy"
              width="120">
          </el-table-column>
          <el-table-column
              prop="numberOfCriticalBugsFoundUntil"
              label="CriticalBugs"
              width="120">
          </el-table-column>
          <el-table-column
              prop="cvsLogEntropy"
              label="LogEntropy"
              width="120">
          </el-table-column>
          <el-table-column
              label="HighPriorityBugs"
              prop="numberOfHighPriorityBugsFoundUntil"
              width="120">
          </el-table-column>
          <el-table-column
              label="MajorBugs"
              prop="numberOfMajorBugsFoundUntil"
              width="120">
          </el-table-column>
          <el-table-column
              label="LinEntropy"
              prop="cvsLinEntropy"
              width="120">
          </el-table-column>
          <el-table-column
              prop="numberOfBugsFoundUntil"
              label="BugsFound"
              width="120">
          </el-table-column>
          <el-table-column
              prop="cvsExpEntropy"
              label="ExpEntropy"
              width="120">
          </el-table-column>

          <el-table-column
              fixed="right"
              prop="predictresult"
              label="结果"
              :filters="[{ text: '通过', value: 'clean' }, { text: '异常', value: 'buggy' }]"
              :filter-method="filterTag"
              filter-placement="bottom-end">
            <template slot-scope="scope">
              <el-tag
                  v-if="scope.row.predictresult === 'clean'"
                  :type="scope.row.predictresult === 'clean' ? 'success' : 'danger'"
                  disable-transitions>通过</el-tag>
              <el-tag
                  v-if="scope.row.predictresult === 'buggy'"
                  :type="scope.row.predictresult === 'clean' ? 'success' : 'danger'"
                  disable-transitions>异常</el-tag>
            </template>
            width="100">
          </el-table-column>
        </el-table>
      </el-dialog>
      <el-dialog title="数据预测详情" :visible.sync="visible2">
        <el-table
            v-loading="loading"
            element-loading-text="模型预测中..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :data="predictData"
            border
            style="width: 100%"
            max-height="600">
          <el-table-column
              fixed
              prop="dataid"
              label="数据标号"
              width="150">
          </el-table-column>
          <el-table-column
              prop="ldhhlcom"
              label="lcom"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhfanin"
              label="Fanin"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhnumberofpublicmethods"
              label="Publicmethods"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhnumberofprivateattributes"
              label="Private_attributes"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhnumberofpublicattributes"
              label="Public_attributes"
              width="120">
          </el-table-column>
          <el-table-column
              label="private_methods"
              prop="ldhhnumberofprivatemethods"
              width="120">
          </el-table-column>
          <el-table-column
              label="attributes_inherited"
              prop="ldhhnumberofattributesinherited"
              width="120">
          </el-table-column>
          <el-table-column
              label="Noc"
              prop="ldhhnoc"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhwmc"
              label="Wmc"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhnumberofattributes"
              label="attributes"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhnumberoflinesofcode"
              label="Linesofcode"
              width="120">
          </el-table-column><el-table-column
            prop="ldhhdit"
            label="Dit"
            width="120">
        </el-table-column><el-table-column
            prop="ldhhfanout"
            label="Fanout"
            width="120">
        </el-table-column>
          <el-table-column
              prop="ldhhnumberofmethodsinherited"
              label="methods_inherited"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhrfc"
              label="Rfc"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhcbo"
              label="Cbo"
              width="120">
          </el-table-column>
          <el-table-column
              prop="ldhhnumberofmethods"
              label="Numberofmethods"
              width="120">
          </el-table-column>



          <el-table-column
              fixed="right"
              prop="predictresult"
              label="结果"
              :filters="[{ text: '通过', value: 'clean' }, { text: '异常', value: 'buggy' }]"
              :filter-method="filterTag"
              filter-placement="bottom-end">
            <template slot-scope="scope">
              <el-tag
                  v-if="scope.row.predictresult === 'clean'"
                  :type="scope.row.predictresult === 'clean' ? 'success' : 'danger'"
                  disable-transitions>通过</el-tag>
              <el-tag
                  v-if="scope.row.predictresult === 'buggy'"
                  :type="scope.row.predictresult === 'clean' ? 'success' : 'danger'"
                  disable-transitions>异常</el-tag>
            </template>
            width="100">
          </el-table-column>
        </el-table>
      </el-dialog>
      <el-dialog title="数据预测详情" :visible.sync="visible3">
        <el-table
            v-loading="loading"
            element-loading-text="模型预测中..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :data="predictData"
            border
            style="width: 100%"
            max-height="600">
          <el-table-column
              fixed
              prop="dataid"
              label="数据标号"
              width="150">
          </el-table-column>
          <el-table-column
              prop="wchunumberofpublicattributes"
              label="Publicattributes"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchunumberofattributes"
              label="Attributes"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchufanin"
              label="Fanin"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchunumberofprivatemethods"
              label="Privatemethods"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchunumberofmethods"
              label="Methods"
              width="120">
          </el-table-column>
          <el-table-column
              label="Private_attributes"
              prop="wchunumberofprivateattributes"
              width="120">
          </el-table-column>
          <el-table-column
              label="Noc"
              prop="wchunoc"
              width="120">
          </el-table-column>
          <el-table-column
              label="Wmc"
              prop="wchuwmc"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchudit"
              label="Dit"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchunumberofattributesinherited"
              label="attributes_inherited"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchufanout"
              label="fanout"
              width="120">
          </el-table-column><el-table-column
            prop="wchulcom"
            label="lcom"
            width="120">
        </el-table-column><el-table-column
            prop="wchurfc"
            label="RFC"
            width="120">
        </el-table-column>
          <el-table-column
              prop="wchunumberofpublicmethods"
              label="Publicmethods"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchucbo"
              label="Cbo"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchunumberofmethodsinherited"
              label="Methods_inherited"
              width="120">
          </el-table-column>
          <el-table-column
              prop="wchunumberoflinesofcode"
              label="Linesofcode"
              width="120">
          </el-table-column>



          <el-table-column
              fixed="right"
              prop="predictresult"
              label="结果"
              :filters="[{ text: '通过', value: 'clean' }, { text: '异常', value: 'buggy' }]"
              :filter-method="filterTag"
              filter-placement="bottom-end">
            <template slot-scope="scope">
              <el-tag
                  v-if="scope.row.predictresult === 'clean'"
                  :type="scope.row.predictresult === 'clean' ? 'success' : 'danger'"
                  disable-transitions>通过</el-tag>
              <el-tag
                  v-if="scope.row.predictresult === 'buggy'"
                  :type="scope.row.predictresult === 'clean' ? 'success' : 'danger'"
                  disable-transitions>异常</el-tag>
            </template>
            width="100">
          </el-table-column>
        </el-table>
      </el-dialog>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">预测</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>


  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      loading:false,
      isvisible:false,
      visible1:false,
      visible2:false,
      visible3:false,
      fileType: ["csv", "txt","xls","xlxs"],
      fileList:[],
      predictData:[],
      user:{},
      form: {
        isdataset:'',
      },
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
  },
  methods: {
    beforeAvatarUpload(file) {
      if (file.type != "" || file.type != null || file.type != undefined){
        //截取文件的后缀，判断文件类型
        const FileExt = file.name.replace(/.+\./, "").toLowerCase();
        //计算文件的大小
        const isLt5M = file.size / 1024 / 1024 < 50; //这里做文件大小限制
        //如果大于50M
        if (!isLt5M) {
          this.$message.error('上传文件大小不能超过 50MB!');
          return false;
        }
        //如果文件类型不在允许上传的范围内
        if(this.fileType.includes(FileExt)){
          return true;
        }
        else {
          this.$message.error("上传文件格式不正确!");
          return false;
        }
      }
    },
    UploadFile () {
      if(this.fileList.length <= 0){
        this.$message.error('请选择文件');
        return
      }
      const formData = new FormData();
      formData.append('file', this.fileList[0].raw)
      // post地址
      this.showResult = true;
    },
    handleChange(file) {
      this.fileList = [file]
    },
    onSubmit() {
      if(this.beforeAvatarUpload(this.fileList[0])==true&&this.form.model){
            this.$message({
              type:"success",
              message:"上传成功",
            })
        if(this.fileList.length <= 0){
          this.$message.error('请选择文件');
          return
        }
        const formData = new FormData();
        formData.append('file', this.fileList[0].raw)
        formData.append('userid', this.user.userid)
        formData.append('model', this.form.model)
        console.log(formData)
        // post地址
        //上传
        if(this.form.isdataset=='0'){
          this.visible1=true
          this.loading=true
          request.post("/api/predict/dataset",formData,{header:{'Content-Type':'multipart/form-data'}}).then(res =>{
            this.predictData=res.data
            this.loading=false
          })
        }
        if(this.form.isdataset=='1'){
          this.visible2=true
          this.loading=true
          request.post("/api/ldhh/dataset",formData,{header:{'Content-Type':'multipart/form-data'}}).then(res =>{
            this.predictData=res.data
            this.loading=false
          })
        }
        if(this.form.isdataset=='2'){
          this.visible3=true
          this.loading=true
          request.post("/api/wchu/dataset",formData,{header:{'Content-Type':'multipart/form-data'}}).then(res =>{
            this.predictData=res.data
            this.loading=false
          })
        }

      }else{
        if(!this.form.model){
          this.$message({
            type:"error",
            message:"请选择模型!",
          })}
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
</style>