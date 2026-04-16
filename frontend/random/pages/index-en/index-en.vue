<!-- <template>
  <view class="container">
    <!-- 标题 -->
    <view class="title-container" :class="{ 'desktop-title-container': isDesktop }">
      <text class="title">ANTI-ECHO WEB</text>
    </view>

    <!-- 导航栏 -->
    <view class="navbar" :class="{ 'desktop-navbar': isDesktop }">
      <button>按钮1</button>
      <button>按钮2</button>
      <button>按钮3</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isDesktop: false
    };
  },
  onReady() {
    // 检测是否为桌面环境
    this.checkDevice();
    // 监听窗口大小变化
    window.addEventListener('resize', this.checkDevice);
  },
  beforeDestroy() {
    // 移除监听
    window.removeEventListener('resize', this.checkDevice);
  },
  methods: {
    checkDevice() {
      // 假设宽度大于768px为桌面环境
      this.isDesktop = window.innerWidth > 768;
    }
  }
};
</script>

<style>
/* 容器样式 */
.container {
  position: relative; /* 使内部绝对定位元素相对于此容器定位 */
  min-height: 100vh; /* 设置容器高度为视口高度 */
}

/* 标题容器样式 */
.title-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
  position: absolute;      /* 绝对定位 */
  top: 25%;                /* 距离顶部四分之一位置 */
  left: 0;
  right: 0;
  background-color: #f8f8f8; /* 背景色 */
  padding: 20px;            /* 内边距 */
  border: 1px solid #ddd;   /* 边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

/* 桌面端标题容器样式 */
.desktop-title-container {
  top: 10%; /* 桌面端可以调整到更高的位置 */
}

/* 标题文本样式 */
.title {
  font-size: 18px; /* 手机端字体大小 */
  color: #333;
}

.desktop-title {
  font-size: 32px; /* 桌面端字体大小 */
}

/* 手机端默认样式 */
.navbar {
  display: flex;
  justify-content: space-around; /* 按钮之间均匀分布 */
  align-items: center;
  position: fixed; /* 固定位置 */
  bottom: 0;       /* 底部对齐 */
  left: 0;
  right: 0;
  background-color: #f8f8f8;
  padding: 10px 0;
  border-top: 1px solid #ddd;
}

/* 桌面端样式 */
.desktop-navbar {
  display: flex; /* 使用Flexbox布局 */
  justify-content: flex-end; /* 右对齐 */
  align-items: center; /* 垂直居中 */
  position: absolute; /* 绝对定位 */
  top: 10px;          /* 距离顶部10px */
  right: 10px;        /* 距离右侧10px */
  background-color: transparent; /* 透明背景 */
  border: none;       /* 无边框 */
}

/* 按钮样式 */
button {
  padding: 10px 20px;
  margin: 0 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  font-size: 14px;
}

/* 使用媒体查询也可以直接设置样式 */
@media (min-width: 768px) {
  .navbar {
    display: flex; /* 使用Flexbox布局 */
    justify-content: flex-end; /* 右对齐 */
    align-items: center; /* 垂直居中 */
    position: absolute; /* 绝对定位 */
    top: 10px;          /* 距离顶部10px */
    right: 10px;        /* 距离右侧10px */
    background-color: transparent; /* 透明背景 */
    border: none;       /* 无边框 */
  }

  .title-container {
    top: 10%; /* 桌面端可以调整到更高的位置 */
  }
}
</style> -->