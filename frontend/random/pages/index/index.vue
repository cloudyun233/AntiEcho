<template>
	<view class="content">
		<!-- 顶部导航栏 -->
		<view class="navbar">
			<text @click="navigateToRandomWord" v-if="currentLanguage === '中文'">每日一词</text>
			<text @click="navigateToRandomWord" v-if="currentLanguage === 'english'">Random Word</text>
			<text>|</text>
			<text @click="changeToCN">简体中文</text>
			<text>|</text>
			<text @click="changeToEN">English</text>
		</view>
		
		<!--主要区域 -->
		<view class="main">

			<view class="text-area">
				<text class="title" v-if="currentLanguage === '中文'">破茧索</text>
				<text class="title1" v-if="currentLanguage === 'english'">ANTI-ECHO WEB</text>
			</view>

			<view class="selector-container">
				<!-- 网站选择 -->
				<view class="selectItem">
					<picker mode="selector" :range="pickerOptions1" @change="onPickerChange1">
						<button class="button">{{ buttonText1 }}</button>
					</picker>
				</view>
				
				<!-- 词库选择 -->
				<view class="selectItem">
					<picker mode="selector" :range="pickerOptions2" @change="onPickerChange2">
						<button class="button">{{ buttonText2 }}</button>
					</picker>

				</view>
			</view>
			<!-- 功能按钮 -->
			<view class="bottom-container">
				<!-- 随机跳转 -->
				<view class="bottomItem">
					<button @click="openExternalUrl" class="button" v-model="inputValue">{{ buttonText3 }}</button>
				</view>
				
				<!-- 获取5个随机词 -->
				<view class="bottomItem">
					<!-- 触发获取词语的按钮 -->
					<button @click="fetchWords" class="button">{{ buttonText4 }}</button>
					<button @click="showDialog" class="small-button">?</button>

				</view>
			</view>

			<!-- 渲染五个按钮，并将词语作为按钮文本 -->
			<view v-if="words.length === 5" class="button-row">
				<button v-for="(word, index) in words" :key="index" @click="onActionButtonClick(word)"
					@longpress="goToGragh(word)" @mousedown="handleMouseDown(word)" @mouseup="handleMouseUp"
					@mouseleave="handleMouseUp" class="btn">
					{{ word }}
				</button>
			</view>
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom">
			<button @click="goToHistory" class="bottom-left-button" v-if="currentLanguage === '中文'">历史记录</button>
			<button @click="goToHistory" class="bottom-left-button"
				v-if="currentLanguage === 'english'">History</button>
			<button @click="goToMembers" class="bottom-right-button" v-if="currentLanguage === '中文'">制作人名单</button>
			<button @click="goToMembers" class="bottom-right-button" v-if="currentLanguage === 'english'">Producer
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
				inputValue: '',
				// 下拉菜单的选项
				pickerOptions1: ['哔哩哔哩', '维基百科', '知乎', '必应'],
				pickerIndex1: 0,
				currentUrl: '',
				pickerOptions2: ['热搜', 'demo_v1'],
				pickerIndex2: 0,
				// 按钮的文本
				buttonText1: '哔哩哔哩(默认)',
				buttonText2: '热搜(默认)',
				buttonText3: '快速跳转',
				buttonText4: '词语骰子',
				showButtons: false,
				words: [],
				pressTimer: null, // 用于存储定时器的ID
				longPressDuration: 1000, // 长按时间阈值，单位为毫秒，例如1000毫秒（1秒）
				wordId: 0
			}
		},
		created() {
			this.checkToken();
			this.checkLanguage(this.options.language)
		},
		methods: {
			checkLanguage(language) {
				if (language === "english") {
					this.changeToEN()
				} else {
					this.changeToCN()
				}
			},
			changeToGraph() {
				uni.redirectTo({
					url: '/pages/graph/graph'
				});
			},
			changeToCN() {
				this.currentLanguage = "中文";
				this.buttonText1 = "Bilibili(默认)";
				this.pickerOptions1 = ['哔哩哔哩', '维基百科', '知乎', '必应'];
				this.buttonText2 = "热搜(默认)";
				this.pickerOptions2 = ['热搜', 'demo_v1'];
				this.buttonText3 = "快速跳转";
				this.buttonText4 = "词语骰子";
			},
			changeToEN() {
				this.currentLanguage = "english";
				this.buttonText1 = "Bilibili(default)";
				this.pickerOptions1 = ['Bilibili', 'Wiki', 'zhihu', 'bing'];
				this.buttonText2 = "Trending topics(default)";
				this.pickerOptions2 = ['Trending topics', 'demo_v1'];
				this.buttonText3 = "Quick jump";
				this.buttonText4 = "Word dice";
			},
			checkToken() {
				const token = this.getToken();
				console.log('token:', token);
				uni.request({
					url: 'http://111.231.191.2:8080/api/checktoken', // 目标URL
					method: 'GET',
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					success: (res) => {
						// 根据实际返回的数据结构调整
						if (res.statusCode === 200) {
							console.log('验证成功');
						} else if (res.statusCode == 401) {
							console.log('获取token');
							const token = res.data;
							localStorage.setItem('jwtToken', token)
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

			openExternalUrl() {
				const ids = {
					'0': 1,
					'1': 5,
				};
				const id = ids[this.pickerIndex2]
				uni.request({
					url: 'http://111.231.191.2:8080/api/random-word', // 目标URL
					method: 'GET',
					data: {
						dictionaryId: id
					},
					success: (res) => {
						// 根据实际返回的数据结构调整
						if (res.statusCode === 200) {
							console.log(res); // 输出响应对象
							console.log(res.data); // 输出响应数据
							const word = res.data; // 替换someKey为实际的键名
							const urls = {
								'0': `https://search.bilibili.com/all?keyword=${word}`,
								'1': `https://zh.wikipedia.org/w/index.php?search=${word}`,
								'2': `https://www.zhihu.com/search?q=${word}`,
								'3': `https://cn.bing.com/search?q=${word}`
							};
							const url = urls[this.pickerIndex1];
							console.log(url);
							window.open(url, '_blank');
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
			//获取令牌
			getToken() {
				// 从本地存储中读取token
				return uni.getStorageSync('jwtToken');
			},
			//保存数据
			saveData(word) {
				const token = this.getToken();
				console.log('token:', token);
				const type = 0;
				const url = `http://111.231.191.2:8080/api/savehistory?word=${encodeURIComponent(word)}&type=${type}`;
				uni.request({
					url: url, // 目标URL
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
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
			// 显示下拉菜单
			showPicker1() {
				this.show1 = true;
			},
			showPicker2() {
				this.show2 = true;
			},
			// 处理下拉菜单的选择变化
			onPickerChange1(e) {
				this.pickerIndex1 = e.detail.value;
				this.buttonText1 = this.pickerOptions1[e.detail.value];
				console.log('你选择的是：', this.buttonText1);
			},
			onPickerChange2(e) {
				this.pickerIndex2 = e.detail.value;
				this.buttonText2 = this.pickerOptions2[e.detail.value];
				console.log('你选择的是：', this.buttonText2);
			},
			goToHistory() {
				uni.navigateTo({
					url: '/pages/history/history?language="chinese"'
				});
			},

			goToMembers() {
				uni.navigateTo({
					url: '/pages/members/members?language="chinese"'
				});
			},
			toggleButtons() {
				this.showButtons = !this.showButtons;
			},
			fetchWords() {
				const ids = {
					'0': 1,
					'1': 5,
				};
				const id = ids[this.pickerIndex2]
				uni.request({
					url: 'http://111.231.191.2:8080/api/five-random-words',
					method: 'GET',
					data: {
						dictionaryId: id // 可选参数，根据需要是否发送
					},
					success: (response) => {
						console.log(response);
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
			onActionButtonClick(word) {
				// 获取选中的下拉菜单选项
				const action = this.pickerIndex1;
				// 获取对应的word
				console.log('你选择的是：', word);
				// 构建URL的字典
				const urls = {
					'0': `https://search.bilibili.com/all?keyword=${word}`,
					'1': `https://zh.wikipedia.org/w/index.php?search=${word}`,
					'2': `https://www.zhihu.com/search?q=${word}`,
					'3': `https://cn.bing.com/search?q=${word}`
				};
				const url = urls[action]
				console.log('你选择的是：', url);
				window.open(url, '_blank');
				this.saveData(word);
			},
			goToGragh(word) {
				if (this.pickerIndex2 === 1) {
					const website = this.pickerIndex1;
					const url = `/pages/graph/graph?param1=${website}&param2=${word}`;
					uni.navigateTo({
						url: url
					});
				}
			},
			navigateToRandomWord() {
				const language = this.currentLanguage;
				uni.redirectTo({
					url: `/pages/randomword/randomword?language=${language}`
				});
			},
			handleMouseDown(word) {
				// 设置定时器，如果用户按住1秒（或设定的长按时间）则触发长按事件
				this.pressTimer = setTimeout(() => {
					this.longPressCallback(word);
				}, this.longPressDuration);
			},
			handleMouseUp() {
				// 清除定时器，如果用户在长按时间阈值内释放，则不触发长按事件
				clearTimeout(this.pressTimer);
			},
			longPressCallback(word) {
				if (this.pickerIndex2 === 1) {
					const website = this.pickerIndex1;
					const url = `/pages/graph/graph?param1=${website}&param2=${word}`;
					uni.navigateTo({
						url: url
					});
				}

			},
			showDialog() {
				uni.showModal({
					title: '提示',
					content: '长按词语按钮可以查看知识图谱',
					showCancel: false, // 不显示取消按钮
					success: (res) => {
						if (res.confirm) {
							console.log('用户点击确定');
						}
					}
				});
			}


		},

	}
</script>

<style>
	.small-button {
		position: absolute; /* 设置为绝对定位 */
		  bottom: 0; /* 顶部对齐 */
		  left: 60%; /* 右侧对齐 */
		  transform: translate(50%, -50%); /* 横向和纵向都移动自身宽度的50%以实现对齐 */
		  font-size: 15px; /* 字体大小 */
		  font-family: YouYuan;
		  font-weight: bold;
		  border: none; /* 无边框 */
		  background-color: #eedece; /* 背景颜色 */
		  color: #363a52; /* 字体颜色 */
		  border-radius: 50%; /* 圆形按钮 */
		  width: 30px;
		  height:30px ;
		  line-height: 30px; /* 行高设置为与按钮高度相同，以垂直居中文字 */
		  cursor: pointer; /* 鼠标悬停时显示指针 */
		  justify-content: center;
		  /* 水平居中 */
	}

	.dropdown-menu {
		display: none;
		position: absolute;
		background-color: #f9f9f9;
		min-width: 160px;
		box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
		z-index: 1;
	}

	.dropdown-menu view {
		padding: 12px 16px;
		text-decoration: none;
		display: block;
		color: black;
	}

	.dropdown-menu view:hover {
		background-color: #f1f1f1;
	}

	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		position: relative;
		height: 95vh;
		background-color: #1f2436;
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
		margin-bottom: 30px;
		width: 60%;
		margin-left: auto;
		/* 左外边距自动 */
		margin-right: auto;
		/* 右外边距自动 */
		background-color: #363a52;
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

	.title {
		font-size: 50px;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}

	.title1 {
		font-size: 50px;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}

	.input {
		border: 1px solid #ccc;
		padding: 10rpx;
		width: 300rpx;
		height: 100rpx;
		box-sizing: border-box;
	}

	.button {
		position: relative;
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

	.button-container {
		display: flex;
		justify-content: space-around;
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

	.bottom-left-button:hover {
		background-color: #3e445b;
	}

	.bottom-right-button:hover {
		background-color: #3e445b;
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

	.button-row {
		display: flex;
		/* 启用Flexbox布局 */
		justify-content: center;
		/* 居中对齐按钮 */
		flex-direction: row;
		/* 设置为列布局 */
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

	.selector-container {
		display: flex;
		flex-direction: column;
		/* 默认为垂直排列 */
		align-items: center;
		/* 水平居中对齐（可选） */
		margin-bottom: 30px;

	}

	.selectItem {
		width: 100%;
		/* 使每个view占满整个容器宽度（可选） */
		margin-bottom: 10px;
		/* 下边距，用于设置两个view之间的间距 */
		text-align: center;
		/* 文本居中对齐（可选） */
	}


	@media (min-width: 768px) {
		.selector-container {
			flex-direction: row;
			/* 改为水平排列 */
			justify-content: space-between;
			/* 在视图之间均匀分布空间 */
		}

		.selectItem {
			width: 48%;
			/* 每个view占据大约一半的宽度 */
			margin-bottom: 0;
			/* 取消垂直间距 */
		}
	}

	.bottom-container {
		display: flex;
		flex-direction: row;
		/* 改为水平排列 */
		justify-content: space-between;
		/* 在视图之间均匀分布空间 */
	}

	.bottomItem {
		width: 48%;
		/* 使每个view占满整个容器宽度（可选） */
		position: relative;
		/* 设置为相对定位 */

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

	.btn {
		flex: 1 1 20%;
		/* 每个按钮占据30%的宽度，允许增长和收缩 */
		min-width: 100px;
		/* 最小宽度 */
		padding: 10px 10px;
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
		height: 30px;
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
			height: 50px;
		}
	}
</style>