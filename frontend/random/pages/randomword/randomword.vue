<template>
	<view class="content">
		<!-- 顶部导航栏 -->
		<view class="navbar">
			<text @click="navigateToIndex" v-if="currentLanguage === '中文'">破茧索</text>
			<text @click="navigateToIndex" v-if="currentLanguage === 'english'">ANTI-ECHO WEB</text>
			<text>|</text>
			<text @click="changeToCN">简体中文</text>
			<text>|</text>
			<text @click="changeToEN">English</text>
		</view>
		<!-- 主要区域 -->
		<view class="main">
			
			<view class="text-area">
				<text class="title" v-if="currentLanguage === '中文'">每日一词</text>
				<text class="title" v-if="currentLanguage === 'english'">Random Word</text>
			</view>
			
			<!-- 词库选择 -->
			<view class="selectItem">
				<picker mode="selector" :range="pickerOptions" @change="onPickerChange2">
					<button class="button" @click="showPicker2">{{ buttonText1 }}</button>
				</picker>
			</view>
			
			<!-- 功能按钮 -->
			<view class="bottom-container">
				
				<!-- 随机单词 -->
				<view class="bottomItem">
					<!-- 按钮用于发送请求并跳转 -->
					<button @click="openExternalUrl" class="button">{{ buttonText2 }}</button>
				</view>
				
				<!-- 获取5个随机单词 -->
				<view class="bottomItem">
					<!-- 触发获取词语的按钮 -->
					<button @click="fetchWords" class="button">{{ buttonText3 }}</button>
				</view>
			</view>
			<!-- 渲染五个按钮，并将词语作为按钮文本 -->
			<view v-if="words.length === 5" class="button-row">
				<button v-for="(word, index) in words" :key="index" @click="onActionButtonClick(index,word)"
					class="btn">{{ word }}</button>
			</view>
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom">
			<button @click="goToHistory" class="bottom-left-button" v-if="currentLanguage === '中文'">历史记录</button>
			<button @click="goToHistory" class="bottom-left-button"
				v-if="currentLanguage === 'english'">History</button>
			<button @click="goToMembers" class="bottom-right-button" v-if="currentLanguage === '中文'">制作人名单</button>
			<button @click="goToHistory" class="bottom-right-button" v-if="currentLanguage === 'english'">Producer
				list</button>
		</view>


	</view>
</template>

<script>
	export default {
		onLoad(options) {
			console.log(options.language);
		},
		data() {
			return {
				currentLanguage: '中文',
				title: '每日一词',
				pickerOptions: ['英语四级', '英语六级', '托福'],
				pickerIndex: 0,
				buttonText1: '英语四级(默认)',
				buttonText2: '随机一词',
				buttonText3: '快速复习',
				showButtons: false,
				words: []
			}
		},
		created() {
			this.checkLanguage(this.options.language)
		},
		methods: {
			checkLanguage(language){
				if(language === "english"){
					this.changeToEN()
				}else{
					this.changeToCN()
				}
			},
			changeToCN() {
				this.currentLanguage = "中文";
				this.pickerOptions = ['英语四级', '英语六级', '托福'];
				this.buttonText1 = "英语四级(默认)";
				this.buttonText2 = "随机一词";
				this.buttonText3 = "快速复习";
			},
			changeToEN() {
				this.currentLanguage = "english";
				this.pickerOptions = ['CET4', 'CET6', 'TOEFL'];
				this.buttonText1 = "CET4(default)";
				this.buttonText2 = "a random word";
				this.buttonText3 = "Quick review";
			},
			getToken() {
				// 从本地存储中读取token
				return uni.getStorageSync('jwtToken');
			},
			navigateToIndex() {
				const language = this.currentLanguage;
				uni.redirectTo({
					url: `/pages/index/index?language=${language}`
				});
			},
			showPicker2() {
				this.show2 = true;
			},
			onPickerChange2(e) {
				this.pickerIndex = e.detail.value;
				this.buttonText1 = this.pickerOptions[e.detail.value];
				console.log('你选择的是：', this.buttonText2);
			},
			openExternalUrl() {
				uni.request({
					url: 'http://111.231.191.2:8080/api/random-word', // 目标URL
					method: 'GET',
					data: {
						dictionaryId: this.pickerIndex + 2 // 可选参数，根据需要是否发送
					},
					success: (res) => {
						// 根据实际返回的数据结构调整
						if (res.statusCode === 200) {
							console.log(res); // 输出响应对象
							console.log(res.data); // 输出响应数据
							const word = res.data; // 替换someKey为实际的键名
							const url =
								'https://dictionary.cambridge.org/zhs/%E8%AF%8D%E5%85%B8/%E8%8B%B1%E8%AF%AD-%E6%B1%89%E8%AF%AD-%E7%AE%80%E4%BD%93/' +
								word;
							console.log(url);
							location.href = url;
							this.saveData(word);
						} else {
							uni.showToast({
								title: '获取数据失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						uni.showToast({
							title: '请求失败',
							icon: 'none'
						});
					}
				});
			},
			//保存数据
			saveData(word) {
				const token = this.getToken();
				const url = `http://111.231.191.2:8080/api/savehistory?word=${encodeURIComponent(word)}&type=1`;
				uni.request({
					url: url, // 目标URL
					method: 'GET',
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					success: (res) => {
						// 根据实际返回的数据结构调整
						if (res.statusCode === 200) {
							console.log('保存成功');
						} else if (res.statusCode == 401) {
							console.log('验证失败');
						} else {
							uni.showToast({
								title: '保存失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						uni.showToast({
							title: '请求失败',
							icon: 'none'
						});
					}
				})
			},
			fetchWords() {
				uni.request({
					url: 'http://111.231.191.2:8080/api/five-random-words',
					method: 'GET',
					data: {
						dictionaryId: this.pickerIndex + 2 // 可选参数，根据需要是否发送
					},
					success: (response) => {
						if (response.statusCode === 200 && response.data) {
							// 假设接口返回的是一个包含五个词语的数组
							this.words = response.data;
						} else {
							uni.showToast({
								title: '获取词语失败',
								icon: 'none'
							});
						}
					},
					fail: (error) => {
						console.error('请求失败:', error);
						uni.showToast({
							title: '请求失败',
							icon: 'none'
						});
					}
				});
			},
			onActionButtonClick(index, word) {
				// 获取对应的word
				console.log('你选择的是：', word);
				// 构建URL的字典
				const url =
					'https://dictionary.cambridge.org/zhs/%E8%AF%8D%E5%85%B8/%E8%8B%B1%E8%AF%AD-%E6%B1%89%E8%AF%AD-%E7%AE%80%E4%BD%93/' +
					word;
				console.log('你选择的是：', url);
				location.href = url;
				this.saveData(word);
			},
			goToHistory() {
				uni.navigateTo({
					url: '/pages/history/history_en/history_en'
				});
			},

			goToMembers() {
				uni.navigateTo({
					url: '/pages/members/members'
				});
			},

		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 95vh;
		background-color: #1f2436;
	}

	.title {
		font-size: 50px;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}


	.text-area {
		display: flex;
		justify-content: center;
		background-color: #363a52;
	}

	.input {
		border: 1px solid #ccc;
		padding: 10rpx;
		width: 300rpx;
		height: 100rpx;
		box-sizing: border-box;
	}


	.navbar {
		display: flex;
		/* 使用Flexbox布局 */
		justify-content: flex-end;
		/* 右对齐 */
		position: absolute;
		/* 绝对定位 */
		top: 10px;
		/* 距离顶部10px */
		background-color: transparent;
		/* 透明背景 */
		border: none;
		/* 无边框 */
	}

	@media (min-width: 768px) {
		.navbar {
			display: flex;
			/* 使用Flexbox布局 */
			justify-content: flex-end;
			/* 右对齐 */
			position: absolute;
			/* 绝对定位 */
			top: 10px;
			/* 距离顶部10px */
			right: 10px;
			/* 距离右边10px */
			background-color: transparent;
			/* 透明背景 */
			border: none;
			/* 无边框 */
		}
	}


	.navbar text {
		padding: 5px 15px;
		/* 按钮内边距 */
		border: none;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;

	}

	.navbar text:hover {
		color: #a89c89;
		/* 鼠标悬停时按钮的背景颜色 */
	}

	.navbar text:active {
		color: #4e536b;
	}

	.main {
		position: absolute;

		top: 50px;
		width: 100%;
	}

	@media (min-width: 768px) {
		.main {
			position: absolute;
			top: 100px;
		}
	}


	.text-area {
		display: flex;
		justify-content: center;
		align-items: center;
		/* 确保垂直居中 */
		border: 2px solid #000;
		/* 设置边框颜色和宽度 */
		padding: 30px 100px;
		/* 内边距，让文本离边框有一定距离 */
		border-radius: 5px;
		/* 圆角效果，可选 */
		margin-bottom: 20px;
		width: 60%;
		margin-left: auto;
		/* 左外边距自动 */
		margin-right: auto;
		/* 右外边距自动 */
	}

	@media (max-width: 768px) {
		.text-area {
			/* 小屏幕时全宽 */
			padding: 20px;
			/* 减少内边距 */
		}
	}

	@media (min-width: 768px) {
		.text-area {
			max-width: 600px;
			/* 大屏幕时最大宽度 */
		}
	}

	.button {
		display: flex;
		justify-content: center;
		/* 水平居中 */
		align-items: center;
		/* 垂直居中 */
		width: 200px;
		height: 80px;
		padding: 15px 30px;
		/* 内边距 */
		border: none;
		/* 移除默认边框 */
		background-color: #363a52;
		/* 背景颜色 */
		color: #eedece;
		/* 文字颜色 */
		font-size: 18px;
		/* 文字大小 */
		border-radius: 5px;
		/* 圆角 */
		cursor: pointer;
		/* 鼠标悬停时显示为指针 */
		font-family: YouYuan;
		font-weight: bold;
	}

	.button:hover {
		background-color: #3e445b;
	}

	.button:active {
		background-color: #4e536b;
		/* 按下时背景颜色变深 */
	}

	.selectItem {
		width: 100%;
		/* 使每个view占满整个容器宽度（可选） */
		margin-bottom: 10px;
		/* 下边距，用于设置两个view之间的间距 */
		text-align: center;
		/* 文本居中对齐（可选） */
	}


	.bottom-container {
		display: flex;
		flex-direction: row;
		/* 改为水平排列 */
		justify-content: space-between;
		/* 在视图之间均匀分布空间 */
		margin-bottom: 50px;
	}

	.bottomItem {
		width: 48%;
		/* 使每个view占满整个容器宽度（可选） */
	}

	.button-row {
		display: flex;
		flex-wrap: wrap;
		/* 允许换行 */
		justify-content: center;
		/* 水平居中对齐 */
		gap: 10px;
		/* 按钮之间的间距 */
		padding: 20px;
		/* 容器内边距 */
	}
	.button-row button {
		margin: 0 5px;
		/* 为每个按钮添加外边距 */
		width: 300rpx;
		height: 100rpx;
		background-color: #363a52;
		color: #e8752e;
		font-family: YouYuan;
		font-weight: bold;
		font-size: 18px;
	}
	.button-row button:hover {
		background-color: #3e445b;
	}
	
	.button-row button:active {
		background-color: #4e536b;
	}

	.btn {
		flex: 1 1 30%;
		/* 每个按钮占据30%的宽度，允许增长和收缩 */
		min-width: 100px;
		/* 最小宽度 */
		padding: 10px 20px;
		/* 内边距 */
		border: none;
		/* 移除默认边框 */
		background-color: #007bff;
		/* 背景颜色 */
		color: white;
		/* 文字颜色 */
		font-size: 16px;
		/* 文字大小 */
		border-radius: 5px;
		/* 圆角 */
		cursor: pointer;
		/* 鼠标悬停时显示为指针 */
	}

	.btn:active {
		background-color: #0056b3;
		/* 按下时背景颜色变深 */
	}

	@media (min-width: 768px) {
		.button-row {
			flex-wrap: nowrap;
			/* 不换行 */
		}

		.btn {
			flex: 1 1 20%;
			/* 每个按钮占据20%的宽度，允许增长和收缩 */
			min-width: 0;
			/* 取消最小宽度限制 */
		}
	}

	.bottom-left-button {
		position: absolute;
		left: 10px;
		/* 距离左边0距离 */
		bottom: 10px;
		/* 距离底部0距离 */
		background-color: #363a52;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
		font-size: 18px;
	}

	.bottom-right-button {
		position: absolute;
		right: 10px;
		/* 距离左边0距离 */
		bottom: 10px;
		/* 距离底部0距离 */
		background-color: #363a52;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
		font-size: 18px;
	}

	.bottom-left-button:hover {
		background-color: #3e445b;
	}

	.bottom-right-button:hover {
		background-color: #3e445b;
	}

	.bottom-left-button:active {
		background-color: #4e536b;
	}

	.bottom-right-button:active {
		background-color: #4e536b;
	}
</style>