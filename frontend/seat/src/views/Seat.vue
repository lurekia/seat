<template>
  <div class="seat-container">
    <!-- 搜索和操作区域 -->
    <div class="operation-section">
      <div class="search-box">
        <el-input
          v-model="name"
          placeholder="请输入座位名称"
          prefix-icon="el-icon-search"
          clearable
          @clear="load"
        >
          <el-button slot="append" icon="el-icon-search" @click="load">搜索</el-button>
        </el-input>
        <el-select v-model="status" placeholder="座位状态" clearable @change="load">
          <el-option label="空闲" value="AVAILABLE"></el-option>
          <el-option label="使用中" value="IN_USE"></el-option>
          <el-option label="维修中" value="MAINTENANCE"></el-option>
          <el-option label="已预约" value="RESERVED"></el-option>
        </el-select>
        <el-button type="info" plain @click="reset">重置</el-button>
      </div>
      
      <div class="action-box">
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i> 新增座位
        </el-button>
        <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-warning"
          icon-color="red"
          title="确定要批量删除选中的座位吗？"
          @confirm="delBatch"
        >
          <el-button type="danger" slot="reference">
            <i class="el-icon-delete"></i> 批量删除
          </el-button>
        </el-popconfirm>
      </div>
    </div>

    <!-- 座位展示区域 -->
    <div class="seat-grid">
      <el-row :gutter="20">
        <el-col :span="6" v-for="seat in tableData" :key="seat.id">
          <el-card class="seat-card" :body-style="{ padding: '0px' }" shadow="hover">
            <div class="seat-image">
              <el-image :src="seat.img" fit="cover">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <div class="seat-status" :class="getStatusClass(seat.state)">
                {{ getStatusText(seat.state) }}
              </div>
            </div>
            <div class="seat-info">
              <h3>{{ seat.name }}</h3>
              <p>{{ seat.info }}</p>
              <div class="seat-user" v-if="seat.userName">
                <i class="el-icon-user"></i>
                <span>{{ seat.userName }}</span>
              </div>
              <div class="seat-time" v-if="seat.startTime && seat.endTime">
                <i class="el-icon-time"></i>
                <span>{{ seat.startTime }} - {{ seat.endTime }}</span>
              </div>
            </div>
            <div class="seat-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click="handleEdit(seat)"
                v-if="!seat.userId"
              >
                编辑
              </el-button>
              <el-button 
                type="success" 
                size="small" 
                @click="handleReserve(seat)"
                v-if="seat.state === 'AVAILABLE'"
              >
                预约
              </el-button>
              <el-button 
                type="warning" 
                size="small" 
                @click="handleStartUse(seat)"
                v-if="seat.state === 'RESERVED' && seat.userId === user.id"
              >
                开始使用
              </el-button>
              <el-button 
                type="info" 
                size="small" 
                @click="handleEndUse(seat)"
                v-if="seat.state === 'IN_USE' && seat.userId === user.id"
              >
                结束使用
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="handleMaintenance(seat)"
                v-if="user.role === 'ROLE_ADMIN'"
              >
                维修
              </el-button>
              <el-button 
                type="success" 
                size="small" 
                @click="handleAvailable(seat)"
                v-if="user.role === 'ROLE_ADMIN' && seat.state === 'MAINTENANCE'"
              >
                恢复使用
              </el-button>
              <el-popconfirm
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon="el-icon-warning"
                icon-color="red"
                title="确定要删除这个座位吗？"
                @confirm="del(seat.id)"
              >
                <el-button 
                  type="danger" 
                  size="small" 
                  slot="reference"
                  v-if="!seat.userId"
                >
                  删除
                </el-button>
              </el-popconfirm>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-section">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[8, 16, 24, 32]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      >
      </el-pagination>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      :title="form.id ? '编辑座位' : '新增座位'" 
      :visible.sync="dialogFormVisible" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="form" 
        :rules="rules" 
        ref="ruleForm" 
        label-width="100px"
        size="medium"
      >
        <el-form-item label="座位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入座位名称"></el-input>
        </el-form-item>
        <el-form-item label="座位简介" prop="info">
          <el-input 
            type="textarea" 
            v-model="form.info" 
            placeholder="请输入座位简介"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="座位照片" prop="img">
          <el-upload
            class="seat-uploader"
            action="http://localhost:9090/file/upload"
            :show-file-list="false"
            :on-success="handleImgUploadSuccess"
            :before-upload="beforeImgUpload"
          >
            <img v-if="form.img" :src="form.img" class="uploaded-image">
            <i v-else class="el-icon-plus uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

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
  name: "Seat",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 8,
      name: "",
      status: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: '请输入座位名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        info: [
          { required: true, message: '请输入座位简介', trigger: 'blur' }
        ],
        img: [
          { required: true, message: '请上传座位照片', trigger: 'change' }
        ]
      },
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
    load() {
      this.request.get("/seat/search", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          status: this.status
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    getStatusClass(state) {
      const statusMap = {
        'AVAILABLE': 'status-free',
        'RESERVED': 'status-reserved',
        'IN_USE': 'status-in-use',
        'MAINTENANCE': 'status-maintenance'
      }
      return statusMap[state] || 'status-free'
    },
    getStatusText(state) {
      const statusMap = {
        'AVAILABLE': '空闲',
        'RESERVED': '已预约',
        'IN_USE': '使用中',
        'MAINTENANCE': '维修中'
      }
      return statusMap[state] || '空闲'
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
    handleMaintenance(seat) {
      this.$confirm('确定要将此座位设置为维修状态吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.post("/seat/maintenance/" + seat.id).then(res => {
          if (res.code === '200') {
            this.$message.success("设置维修状态成功")
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleAvailable(seat) {
      this.$confirm('确定要将此座位恢复为可用状态吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.request.post("/seat/available/" + seat.id).then(res => {
          if (res.code === '200') {
            this.$message.success("恢复可用状态成功")
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.request.post("/seat", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles()
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles()
        }
      })
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的座位")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/seat/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
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
    handleImgUploadSuccess(res) {
      this.form.img = res
      this.$message.success("上传成功")
    },
    beforeImgUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    }
  }
}
</script>

<style scoped>
.seat-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.operation-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-box .el-input {
  width: 200px;
}

.search-box .el-select {
  width: 120px;
}

.action-box {
  display: flex;
  gap: 10px;
}

.seat-grid {
  margin-bottom: 20px;
}

.seat-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.seat-card:hover {
  transform: translateY(-5px);
}

.seat-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.seat-image .el-image {
  width: 100%;
  height: 100%;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

.seat-status {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 8px;
  border-radius: 4px;
  color: white;
  font-size: 12px;
}

.status-free {
  background-color: #409EFF;
}

.status-reserved {
  background-color: #67C23A;
}

.status-in-use {
  background-color: #F56C6C;
}

.status-maintenance {
  background-color: #909399;
}

.seat-info {
  padding: 15px;
}

.seat-info h3 {
  margin: 0 0 10px;
  font-size: 18px;
  color: #303133;
}

.seat-info p {
  margin: 0 0 10px;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.seat-user, .seat-time {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #909399;
  font-size: 14px;
  margin-bottom: 5px;
}

.seat-actions {
  padding: 10px 15px;
  border-top: 1px solid #EBEEF5;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.seat-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.seat-uploader:hover {
  border-color: #409EFF;
}

.uploader-icon {
  font-size: 28px;
  color: #409EFF;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.uploaded-image {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

:deep(.el-dialog__body) {
  padding: 20px 30px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-button--primary) {
  background-color: #409EFF;
  border-color: #409EFF;
}

:deep(.el-button--primary:hover),
:deep(.el-button--primary:focus) {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

:deep(.el-pagination.is-background .el-pager li:not(.disabled).active) {
  background-color: #409EFF;
}

:deep(.el-pagination.is-background .el-pager li:not(.disabled):hover) {
  color: #409EFF;
}

:deep(.el-dialog__header) {
  background-color: #f5f7fa;
  padding: 15px 20px;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-dialog__title) {
  color: #409EFF;
  font-weight: 600;
}

:deep(.el-input__inner:focus),
:deep(.el-textarea__inner:focus) {
  border-color: #409EFF;
}

:deep(.el-upload--picture-card:hover) {
  border-color: #409EFF;
  color: #409EFF;
}
</style>
