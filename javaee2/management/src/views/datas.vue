<template>
  <div style="padding: 10px" class="body">
    <!--    <div style="padding: 30px;background:#409EFF "  >-->
    <!--      <p class="sample" style="font-size: 120%;font-weight: bolder;color: white">-->
    <!--        预测记录查询-->
    <!--      </p>-->
    <!--    </div>-->
    <!--      搜索区域-->
    <div style="padding: 10px">
      <el-form :inline="true"  :model="form" class="demo-form-inline">
        <el-form-item label="数据集名称" style="font-weight: bolder">
          <el-input v-model="form.datasetname" placeholder="数据集名称"></el-input>
        </el-form-item>
        <el-form-item label="数据格式" style="font-weight: bolder">
          <el-select v-model="form.datasetKind" placeholder="数据格式">
            <el-option label="不指定" value=""></el-option>
            <el-option label="单条数据" value="single"></el-option>
            <el-option label=".txt" value="txt"></el-option>
            <el-option label=".csv" value="csv"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用模型" style="font-weight: bolder">
          <el-select v-model="form.model" placeholder="使用模型">
            <el-option label="不指定" value=""></el-option>
<!--            <el-option label="朴素贝叶斯" value="beysi"></el-option>-->
            <el-option label="逻辑回归" value="logistic"></el-option>
            <el-option label="支持向量机" value="svm"></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="评价指标" style="font-weight: bolder">-->
<!--          <el-select v-model="form.isdataset" placeholder="使用模型">-->
<!--            <el-option label="不指定" value= ></el-option>-->
<!--            <el-option label="CVS" value="0"></el-option>-->
<!--            <el-option label="LDHH" value="1"></el-option>-->
<!--            <el-option label="WCHU" value="2"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button type="primary" @click="onSubmit" >查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--    表单显示区-->
    <el-table
        :data="tableData"
        style="width: 100%"
        max-height="600">
      <el-table-column
          fixed
          prop="datasetname"
          label="数据名称"
          width="200"
      >
      </el-table-column>
      <el-table-column
          prop="uploadtime"
          label="上传时间"
          width="200">
      </el-table-column>
      <el-table-column
          prop="datasetKind"
          label="类型"
          width="120">
      </el-table-column>
      <el-table-column
          prop="model"
          label="使用模型"
          width="120">
      </el-table-column>
      <el-table-column
          prop="isdataset"
          label="评价指标"
          width="80">
      </el-table-column>


      <el-table-column
          label="操作"
          width="180">
        <template slot-scope="scope">
          <el-button
              @click="sign_contract(scope.row)"
              type="text"
              size="small"
              v-loading.fullscreen.lock="fullscreenLoading">
            查看预测结果
          </el-button>
          <el-button
              @click="del(scope.$index,scope.row)"
              type="text"
              size="small">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--    翻页-->
    <div class="block">
      <span class="demonstration">显示总数</span>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage1"
          :page-size="100"
          layout="total, prev, pager, next"
          :total=total>
      </el-pagination>
    </div>

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

  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  data(){
    return{
      loading: false,
      visible1:false,
      visible2:false,
      visible3:false,
      user:{},
      total:0,
      pageNum:1,
      PageSize:10,
      predictData:[],
      form:{
        model:''
      },
      tableData:[
      ]
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(userStr)
    this.load()
  },
  methods: {
    openFullScreen1() {
      this.fullscreenLoading = true;
    },
    filterTag(value, row) {
      return row.predictresult === value;
    },
    del(index,row){
      this.$confirm("永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
          .then(() => {
            // 移除对应索引位置的数据，可以对row进行设置向后台请求删除数据
            this.tableData.splice(index, 1);
            request.get("/api/datasets_center/deleteHistory",{
              params:{
                datasetid:row.datasetid
              }}).then(res =>{
              console.log(res)
            })
            this.$message({
              type: "success",
              message: "删除成功!"
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
    },
    load(){
      request.get("/api/datasets_center/none",{
        params:{
          pageNum:1,
          pageSize: 100,
          username:this.user.username}
      }).then(res =>{
        console.log(res)
        console.log("request")
        this.tableData = res.data.records
        this.total=res.data.total
        for(var i =0;i<res.data.total;i++){
          if(this.tableData[i].isdataset=='0'){this.tableData[i].isdataset='CVS'}
          if(this.tableData[i].isdataset=='1'){this.tableData[i].isdataset='LDHH'}
          if(this.tableData[i].isdataset=='2'){this.tableData[i].isdataset='WCHU'}
        }
      })
    },
    sign_contract(row) {
      console.log(row.datasetid)
      console.log(row.datasetname)
      this.isvisible=true
      request.get("/api/datasets_center/datasetResult",{
        params:{
          datasetid:row.datasetid
        }}).then(res =>{
        this.predictData=res.data
        if(res.data[0].ldhhlcom||res.data[0].ldhhlcom=='0'){
          this.visible2=true;
        }
        else if(res.data[0].wchufanin||res.data[0].wchufanin=='0'){
          this.visible3=true;
        }
        else{
          this.visible1=true;
        }
      })
    },
    onSubmit(){
      console.log(this.form),
          request.get("/api/datasets_center/search",
              {
                params:{
                  pageNum:1,
                  pageSize: 10,
                  username:this.user.username,
                  datasetname:this.form.datasetname,
                  datasetKind:this.form.datasetKind,
                  model:this.form.model,
                }
              }).then(res =>{
            this.fullscreenLoading=false
            console.log(res)
            console.log("request")
            this.tableData = res.data.records
            this.total=res.data.total
            for(var i =0;i<res.data.total;i++){
              if(this.tableData[i].isdataset=='0'){this.tableData[i].isdataset='CVS'}
              if(this.tableData[i].isdataset=='1'){this.tableData[i].isdataset='LDHH'}
              if(this.tableData[i].isdataset=='2'){this.tableData[i].isdataset='WCHU'}
            }
          })
    },
  },

}
</script>

<style scoped>
.block{
  position: absolute;
  top:80%;

}
.body{
  padding: 10px;height: auto;margin-right: 15px;
  border-radius: 25px 25px 25px 25px ;
  box-shadow: 2px 2px 10px #06C;
}
.box-card {
  width: 480px;
}

</style>