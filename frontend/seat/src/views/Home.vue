<template>
  <div class="home-container">
    <!-- 轮播图部分 -->
    <div class="carousel-section">
      <el-carousel height="500px" :interval="5000" arrow="always" indicator-position="outside">
        <el-carousel-item v-for="item in imgs" :key="item.id">
          <div class="carousel-content">
            <img :src="item.src" :alt="item.title">
            <div class="carousel-info">
              <h2>{{ item.title }}</h2>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 功能区域 -->
    <div class="features-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">
              <i class="el-icon-date"></i>
            </div>
            <h3>座位预约</h3>
            <p>在线预约图书馆座位，方便快捷</p>
            <el-button type="primary" @click="$router.push('/front/seat')">立即预约</el-button>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">
              <i class="el-icon-bell"></i>
            </div>
            <h3>通知公告</h3>
            <p>及时了解图书馆最新通知</p>
            <el-button type="primary" @click="$router.push('/front/notice')">查看通知</el-button>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">
              <i class="el-icon-user"></i>
            </div>
            <h3>个人中心</h3>
            <p>管理个人信息和预约记录</p>
            <el-button type="primary" @click="$router.push('/front/person')">进入中心</el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 公告区域 -->
    <div class="notice-section">
      <el-card class="notice-card">
        <div slot="header" class="notice-header">
          <span>最新公告</span>
          <el-button type="text" @click="$router.push('/front/notice')">更多</el-button>
        </div>
        <div class="notice-list">
          <div v-for="notice in notices" :key="notice.id" class="notice-item">
            <span class="notice-title">{{ notice.title }}</span>
            <span class="notice-time">{{ notice.time }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 页脚信息 -->
    <div class="footer-section">
      <el-card class="footer-card">
        <p>前端开发：<b>2021211080徐桢阳</b></p>
        <p>后端开发：<b>2021211078谢腾</b>、<b>2021211081闫子豪</b></p>
        <p>数据库开发：三人共同开发</p>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      imgs: [
        { 
          id: 1,
          src: require('@/assets/tiger.jpg'),
          title: '图书馆环境',
          description: '安静舒适的学习环境'
        },
        { 
          id: 2,
          src: require('@/assets/sheep.jpg'),
          title: '座位预约',
          description: '便捷的在线预约系统'
        },
        { 
          id: 3,
          src: require('@/assets/cat.jpg'),
          title: '学习空间',
          description: '打造理想的学习氛围'
        }
      ],
      notices: [
        { id: 1, title: '图书馆开放时间调整通知', time: '2024-03-20' },
        { id: 2, title: '座位预约系统升级公告', time: '2024-03-19' },
        { id: 3, title: '图书馆文明公约', time: '2024-03-18' }
      ]
    }
  },
  created() {
    // 获取最新公告
    this.loadNotices()
  },
  methods: {
    loadNotices() {
      this.request.get("/notice/page", {
        params: {
          pageNum: 1,
          pageSize: 3
        }
      }).then(res => {
        if (res.code === '200') {
          this.notices = res.data.records.map(item => ({
            id: item.id,
            title: item.title,
            time: item.time
          }))
        }
      })
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.carousel-section {
  width: 80%;
  margin: 0 auto 30px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.carousel-content {
  position: relative;
  height: 100%;
}

.carousel-content img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
  color: white;
}

.carousel-info h2 {
  margin: 0 0 10px;
  font-size: 24px;
}

.carousel-info p {
  margin: 0;
  font-size: 16px;
}

.features-section {
  width: 80%;
  margin: 0 auto 30px;
}

.feature-card {
  text-align: center;
  padding: 20px;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 48px;
  color: #e67e22;
  margin-bottom: 20px;
}

.feature-card h3 {
  margin: 10px 0;
  color: #333;
}

.feature-card p {
  color: #666;
  margin-bottom: 20px;
}

.notice-section {
  width: 80%;
  margin: 0 auto 30px;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notice-list {
  padding: 10px 0;
}

.notice-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.notice-item:last-child {
  border-bottom: none;
}

.notice-title {
  color: #333;
}

.notice-time {
  color: #999;
  font-size: 14px;
}

.footer-section {
  width: 80%;
  margin: 0 auto;
}

.footer-card {
  text-align: center;
  color: #666;
}

.footer-card p {
  margin: 5px 0;
}

:deep(.el-carousel__arrow) {
  background-color: rgba(0,0,0,0.3);
}

:deep(.el-carousel__arrow:hover) {
  background-color: rgba(0,0,0,0.5);
}

:deep(.el-carousel__indicators) {
  bottom: 20px;
}
</style>
