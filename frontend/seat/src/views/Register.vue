<template>
  <div class="wrapper">
    <div class="header">
      <h1>图书馆座位预约系统</h1>
    </div>
    <div class="register-container">
      <div class="register-image">
        <img src="../assets/register.jpg" alt="注册背景">
      </div>
      <div class="register-form">
        <h2>欢迎注册</h2>
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
          <el-form-item prop="confirmPassword">
            <el-input 
              size="large" 
              prefix-icon="el-icon-lock" 
              show-password 
              v-model="user.confirmPassword"
              placeholder="请确认密码">
            </el-input>
          </el-form-item>

          <el-form-item class="button-group">
            <el-button 
              type="primary" 
              size="large" 
              class="register-button" 
              @click="register">
              注册
            </el-button>
          </el-form-item>
          
          <div class="links">
            <el-button type="text" @click="$router.push('/login')">返回登录</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.user.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      user: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("注册成功")
              this.$router.push("/login")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
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

.register-container {
  display: flex;
  margin: 50px auto;
  width: 900px;
  height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  backdrop-filter: blur(10px);
}

.register-image {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.register-image::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(64, 158, 255, 0.2), rgba(30, 136, 229, 0.2));
}

.register-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.register-image:hover img {
  transform: scale(1.05);
}

.register-form {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  background: white;
}

.register-form h2 {
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

.register-button {
  width: 100%;
  height: 45px;
  background: #409EFF;
  border: none;
  font-size: 16px;
  transition: all 0.3s ease;
}

.register-button:hover {
  background: #1E88E5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.links {
  display: flex;
  justify-content: center;
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
</style>