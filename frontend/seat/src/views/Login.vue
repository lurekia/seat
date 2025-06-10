<template>
  <div class="wrapper">
    <div class="header">
      <h1>图书馆座位预约系统</h1>
    </div>
    <div class="login-container">
      <div class="login-image">
        <img src="../assets/login.jpeg" alt="登录背景">
      </div>
      <div class="login-form">
        <h2>欢迎登录</h2>
        <el-form :model="user" :rules="rules" ref="userForm" class="form">
          <el-form-item prop="username">
            <el-input 
              size="large" 
              prefix-icon="el-icon-user" 
              v-model="user.username"
              placeholder="请输入用户名">
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              size="large" 
              prefix-icon="el-icon-lock" 
              show-password 
              v-model="user.password"
              placeholder="请输入密码">
            </el-input>
          </el-form-item>

          <el-form-item class="button-group">
            <el-button 
              type="primary" 
              size="large" 
              class="login-button" 
              @click="login">
              登录
            </el-button>
          </el-form-item>
          
          <div class="links">
            <el-button type="text" @click="$router.push('/register')">注册账号</el-button>
            <el-button type="text" @click="handlePass">找回密码</el-button>
          </div>
        </el-form>
      </div>
    </div>

    <el-dialog 
      title="找回密码" 
      :visible.sync="dialogFormVisible" 
      width="400px"
      center>
      <el-form label-width="80px" size="medium">
        <el-form-item label="用户名">
          <el-input v-model="pass.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="pass.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="passwordBack">重置密码</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {resetRouter, setRoutes} from "@/router";
import Identify from "@/components/Identify";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      pass: {},
      code: '',
      dialogFormVisible: false,
      // 图片验证码
      identifyCode: '',
      // 验证码规则
      identifyCodes: '3456789ABCDEFGHGKMNPQRSTUVWXY',
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  components: {Identify},
  mounted() {
    // 重置路由
    resetRouter()
    this.refreshCode()
  },
  methods: {
    login() {
      if (this.code !== this.identifyCode.toLowerCase()) {
        this.$message({
          type: "error",
          message: "验证码错误"
        })
        return;
      }
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器

              // 动态设置当前用户的路由
              setRoutes()
              if (res.data.role === 'ROLE_USER') {
                this.$router.push("/front/home")
                this.$message.success("登录成功")
              } else {
                this.$router.push("/")
                this.$message.success("登录成功")
              }
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    // // 切换验证码
    // refreshCode() {
    //   this.identifyCode = ''
    //   this.makeCode(this.identifyCodes, 4)
    // },
    // // 生成随机验证码
    // makeCode(o, l) {
    //   for (let i = 0; i < l; i++) {
    //     this.identifyCode += this.identifyCodes[Math.floor(Math.random() * (this.identifyCodes.length))]
    //   }
    // },
    handlePass() {
      this.dialogFormVisible = true
      this.pass = {}
    },
    passwordBack() {
      this.request.put("/user/reset", this.pass).then(res => {
        if (res.code === '200') {
          this.$message.success("重置密码成功，新密码为：123，请尽快修改密码")
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  background: linear-gradient(135deg, #409EFF 0%, #1E88E5 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header {
  width: 100%;
  padding: 20px 0;
  text-align: center;
  color: white;
  background-color: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 500;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.login-container {
  display: flex;
  margin: 50px auto;
  width: 900px;
  height: 500px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  backdrop-filter: blur(10px);
}

.login-image {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.login-image::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(64, 158, 255, 0.2), rgba(30, 136, 229, 0.2));
}

.login-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.login-image:hover img {
  transform: scale(1.05);
}

.login-form {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  background: white;
}

.login-form h2 {
  text-align: center;
  color: #409EFF;
  margin-bottom: 30px;
  font-weight: 500;
}

.form {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.button-group {
  margin-top: 20px;
}

.login-button {
  width: 100%;
  height: 45px;
  background: #409EFF;
  border: none;
  font-size: 16px;
  transition: all 0.3s ease;
}

.login-button:hover {
  background: #1E88E5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.links {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.links .el-button {
  color: #409EFF;
  font-size: 14px;
  transition: all 0.3s ease;
}

.links .el-button:hover {
  color: #1E88E5;
  transform: translateY(-1px);
}

:deep(.el-input__inner) {
  border-radius: 8px;
  border: 1px solid #DCDFE6;
  transition: all 0.3s ease;
}

:deep(.el-input__inner:focus) {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

:deep(.el-input__prefix) {
  color: #409EFF;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

:deep(.el-dialog__body) {
  padding: 30px 20px;
}

:deep(.el-dialog__footer) {
  padding: 20px;
  border-top: 1px solid #eee;
}
</style>