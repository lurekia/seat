<template>
  <div>

    <el-row :gutter="24" style="margin-top: 20px">
      <el-col :span="16">

        <el-carousel height="500px" :interval="2000">
          <el-carousel-item v-for="item in imgs" :key="item">
            <img :src="item.src" style="width: 100%;height: 100%;">
          </el-carousel-item>
        </el-carousel>

      </el-col>

      <el-col :span="8">
        <el-card style="width: 80%;">
          <h2 style="margin: 20px 0">公告列表</h2>
          <div >
            <el-timeline  reverse slot="reference">
              <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
                <el-popover
                    placement="right"
                    width="200"
                    trigger="hover"
                    :content="item.content">
                  <span slot="reference">{{ item.name }}</span>
                </el-popover>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24">
      <div class="search-box" style="margin: 20px 30px">
        <el-input
          v-model="name"
          placeholder="请输入座位名称"
          prefix-icon="el-icon-search"
          clearable
          @clear="load"
          style="width: 200px"
        >
          <el-button slot="append" icon="el-icon-search" @click="load">搜索</el-button>
        </el-input>
        <el-select v-model="status" placeholder="座位状态" clearable @change="load" style="width: 120px; margin-left: 10px">
          <el-option label="空闲" value="AVAILABLE"></el-option>
          <el-option label="使用中" value="IN_USE"></el-option>
          <el-option label="维修中" value="MAINTENANCE"></el-option>
          <el-option label="已预约" value="RESERVED"></el-option>
        </el-select>
        <el-button type="info" plain @click="reset" style="margin-left: 10px">重置</el-button>
      </div>

      <el-table :data="tableData" stripe style="border-radius: 10px; margin: 0 30px 30px; line-height: 30px; width: calc(100% - 60px)">
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="info" label="简介"></el-table-column>
        <el-table-column label="图片">
          <template slot-scope="scope">
            <el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="state" label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.state)">
              {{ getStatusText(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="使用用户" align="center"></el-table-column>
        <el-table-column label="使用时间" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.startTime && scope.row.endTime">
              {{ scope.row.startTime }} - {{ scope.row.endTime }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button 
              type="success" 
              size="small" 
              @click="handleReserve(scope.row)"
              v-if="scope.row.state === 'AVAILABLE'"
            >
              预约
            </el-button>
            <el-button 
              type="warning" 
              size="small" 
              @click="handleStartUse(scope.row)"
              v-if="scope.row.state === 'RESERVED' && scope.row.userId === user.id"
            >
              开始使用
            </el-button>
            <el-button 
              type="info" 
              size="small" 
              @click="handleEndUse(scope.row)"
              v-if="scope.row.state === 'IN_USE' && scope.row.userId === user.id"
            >
              结束使用
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="padding: 10px 0; margin-left: 30px;">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20, 30]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

    </el-row>

    <!-- 预约对话框 -->
    <el-dialog
      title="预约座位"
      :visible.sync="reserveDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="reserveForm" :rules="reserveRules" ref="reserveForm" label-width="100px">
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="reserveForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="reserveForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="reserveDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmReserve">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      notices:[],
      tableData: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      status: "",
      imgs:[{ 
          src: require('@/assets/seat1.jpg') 
        }, { 
          src: require('@/assets/seat2.jpg') 
        }, { 
          src: require('@/assets/seat3.jpg') 
        }],
      reserveDialogVisible: false,
      reserveForm: {
        startTime: '',
        endTime: ''
      },
      reserveRules: {
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ]
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      currentSeat: null
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/notice").then(res=>{
        this.notices=res?.data
      })
      this.request.get("/seat/search", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          status: this.status
        }
      }).then(res=>{
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    getStatusType(state) {
      const statusMap = {
        'AVAILABLE': 'success',
        'RESERVED': 'warning',
        'IN_USE': 'danger',
        'MAINTENANCE': 'info'
      }
      return statusMap[state] || 'info'
    },
    getStatusText(state) {
      const statusMap = {
        'AVAILABLE': '空闲',
        'RESERVED': '已预约',
        'IN_USE': '使用中',
        'MAINTENANCE': '维修中'
      }
      return statusMap[state] || '未知'
    },
    handleReserve(seat) {
      this.currentSeat = seat
      this.reserveDialogVisible = true
      this.reserveForm = {
        startTime: '',
        endTime: ''
      }
    },
    confirmReserve() {
      this.$refs['reserveForm'].validate((valid) => {
        if (valid) {
          this.request.post("/seat/reserve/" + this.currentSeat.id, null, {
            params: {
              startTime: this.reserveForm.startTime,
              endTime: this.reserveForm.endTime
            }
          }).then(res => {
            if (res.code === '200') {
              this.$message.success("预约成功")
              this.reserveDialogVisible = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    handleStartUse(seat) {
      this.$confirm('确定要开始使用这个座位吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.request.post("/seat/start-use/" + seat.id).then(res => {
          if (res.code === '200') {
            this.$message.success("开始使用成功")
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleEndUse(seat) {
      this.$confirm('确定要结束使用这个座位吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post("/seat/end-use/" + seat.id).then(res => {
          if (res.code === '200') {
            this.$message.success("结束使用成功")
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    reset() {
      this.name = ""
      this.status = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    del(id) {
      this.request.delete("/seat/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
  }
}
</script>

<style scoped>
.search-box {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.el-table {
  margin-top: 20px;
}

.el-tag {
  margin-right: 10px;
}

.el-tag--success {
  background-color: #f0f9eb;
  border-color: #e1f3d8;
  color: #67c23a;
}

.el-tag--warning {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}

.el-tag--danger {
  background-color: #fef0f0;
  border-color: #fde2e2;
  color: #f56c6c;
}

.el-tag--info {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}
</style>
