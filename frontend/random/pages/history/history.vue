<template>
	<view class="uni-padding-wrap">

		<text class="clear" @click="clearHistory()">清空历史记录</text>
		<!-- 表格 -->
		<table class="uni-table">
			<tr>
				<th>Word</th>
				<th>Timestamp</th>
				<th colspan="2">操作</th>
			</tr>
			<tr v-for="(item, index) in paginatedHistoryList" :key="index">
				<td>{{ item.word }}</td>
				<td>{{ formatDate(item.timestamp) }}</td>
				<td>
					<!-- 下拉菜单 -->
					<picker mode="selector" :range="actions" @change="onActionChange($event, index)">
						<button>{{ selectedActions[index] || '哔哩哔哩(默认)' }}</button>
					</picker>

				</td>
				<td>
					<!-- 按钮 -->
					<button @click="onActionButtonClick(index,item)">前往</button>
				</td>
			</tr>
		</table>

		<!-- 分页控件 -->
		<view class="pagination">
			<view class="pagination-buttons">
				<button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
				<span>第 {{ currentPage }} 页，共 {{ Math.ceil(totalItems / pageSize) }} 页</span>
				<button @click="changePage(currentPage + 1)"
					:disabled="currentPage === Math.ceil(totalItems / pageSize)">下一页</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				allHistoryList: [], // 缓存所有历史记录
				historyList: [], // 当前页的历史记录
				currentPage: 1,
				pageSize: 10, // 每页显示10行
				totalItems: 0,
				actions: ['哔哩哔哩', '维基百科', '知乎', '必应'], // 下拉菜单选项
				selectedActions: {}, // 存储每行选择的下拉菜单选项
				selectedIndex: {},
			};
		},
		created() {
			this.fetchHistoryData();
		},
		computed: {
			// 计算当前页的历史记录
			paginatedHistoryList() {
				const start = (this.currentPage - 1) * this.pageSize;
				const end = this.currentPage * this.pageSize;
				return this.allHistoryList.slice(start, end);
			}
		},
		methods: {
			getToken() {
				// 从本地存储中读取token
				return uni.getStorageSync('jwtToken');
			},
			fetchHistoryData() {
				const token = this.getToken();
				console.log('token:', token);
				const type = 0;
				const url = `http://111.231.191.2:8080/api/gethistory?type=${type}`;
				// 使用uni.request调用后端API
				uni.request({
					url: url, // 你的API端点
					method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`
					},
					success: (response) => {
						if (response.statusCode === 200) {
							console.log(response);
							this.allHistoryList = response.data; // 缓存所有历史记录
							this.totalItems = this.allHistoryList.length; // 更新总条目数
						} else {
							uni.showToast({
								title: '获取数据失败',
								icon: 'none'
							});
						}
					},
					fail: (error) => {
						console.error('获取History数据失败:', error);
						uni.showToast({
							title: '请求失败',
							icon: 'none'
						});
					}
				});
			},
			formatDate(timestamp) {
				// 如果timestamp是LocalDateTime对象，转换为字符串
				return timestamp.toLocaleString(); // 或者使用你选择的日期库进行格式化
			},
			changePage(page) {
				if (page < 1 || page > Math.ceil(this.totalItems / this.pageSize)) {
					return;
				}
				this.currentPage = page;
			},
			onActionChange(e, index) {
				// 更新选中的下拉菜单选项
				this.selectedActions[index] = this.actions[e.detail.value];
				this.selectedIndex[index] = e.detail.value;
				this.$forceUpdate();
				console.log('你选择的是：', this.selectedActions[index]);
				console.log('你选择的是：', this.selectedIndex[index]);
			},
			onActionButtonClick(index, item) {
				// 获取对应的word
				const word = item.word;
				console.log('你选择的是：', word);

				// 构建URL的字典
				const urls = {
					'0': `https://search.bilibili.com/all?keyword=${word}`,
					'1': `https://zh.wikipedia.org/w/index.php?search=${word}`,
					'2': `https://www.zhihu.com/search?q=${word}`,
					'3': `https://cn.bing.com/search?q=${word}`
				};
				const url = urls[this.selectedIndex[index]]
				console.log('你选择的是：', url);
				window.open(url, '_blank');
			},
			clearHistory() {
				const token = this.getToken();
				uni.request({
					url: 'http://111.231.191.2:8080/api/clearhistory?type=0', // 你的API端点
					method: 'GET',
					header: {
						Authorization: `Bearer ${token}`
					},
					success: (response) => {
						if (response.statusCode === 200) {
							console.log('清除成功');
							location.reload();
						} else {
							uni.showToast({
								title: '获取数据失败',
								icon: 'none'
							});
						}
					},
					fail: (error) => {
						console.error('清空失败:', error);
						uni.showToast({
							title: '请求失败',
							icon: 'none'
						});
					}
				});
			}
		}
	};
</script>

<style>
	.uni-padding-wrap {
		background-color: #1f2436;
		min-height: 100vh;
	}

	.uni-table {
		width: 100%;
		border-collapse: collapse;

	}

	.uni-table th,
	.uni-table td {
		border: 1px solid #0d101b;
		padding: 8px;
		text-align: left;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}

	.pagination {
		position: fixed;
		/* 固定定位 */
		bottom: 20rpx;
		/* 距离底部0 */
		left: 0;
		/* 距离左侧0 */
		width: 100%;
		/* 宽度100% */
		display: flex;
		justify-content: center;
	}

	button {
		padding: 5px 10px;
		font-family: YouYuan;
		font-weight: bold;
		background-color: #363a52;
		color: #eedece;
	}
	button:hover{
		background-color: #3e445b;
	}
	button:active{
		background-color: #4e536b;
	}

	input[type="number"] {
		margin: 0 10px;
		padding: 5px;
	}

	.pagination-buttons {
		display: flex;
		justify-content: space-between;

	}

	.pagination-buttons button {
		flex-grow: 1;
		/* 确保按钮有足够的空间 */
		width: 120px;
		height: 50px;
		font-family: YouYuan;
		font-weight: bold;
		background-color: #363a52;
		color: #eedece;
	}

	.pagination-buttons button:hover {
		background-color: #3e445b;
	}

	.pagination-buttons button:active {
		background-color: #4e536b;
	}

	.pagination-buttons span {
		flex-grow: 2;
		/* 给页码信息更多的空间 */
		text-align: center;
		margin-top: 12px;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}

	.clear {
		display: flex;
		/* 使用Flexbox布局 */
		justify-content: flex-end;
		/* 右对齐 */
		padding: 5px 15px;
		/* 按钮内边距 */
		border: none;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}

	.clear:hover {
		color: #a89c89;
		/* 鼠标悬停时按钮的背景颜色 */
	}
</style>