<template>
	<div class="content">
		<div class="title">
			<p >知识图谱</p>
		</div>
		<div style="height:calc(100vh - 60px);">
			<RelationGraph ref="graphRef" :options="graphOptions" :on-node-click="onNodeClick"
				:on-line-click="onLineClick" />
		</div>
	</div>
</template>

<script>
	import RelationGraph from 'relation-graph'
	export default {
		onLoad(options) {
			console.log(options.param1);
			console.log(options.param2);
		},
		name: 'Demo',
		components: {
			RelationGraph
		},
		data() {
			return {
				graphOptions: {
					defaultJunctionPoint: 'border',
					// 参考"Graph 图谱"中的参数进行设置 https://www.relation-graph.com/#/docs/graph
					defaultLineWidth: 2,
					backgroundColor: '#1f2436',
					defaultNodeColor: '#363a52',
					defaultNodeFontColor: '#eedece',
					defaultLineColor:'#ffea99',
					checkedLineColor:'#4e536b',
					defaultLineMarker: {
						"markerWidth": "0",
						"markerHeight": "0",
						"refX": 6,
						"refY": 6,
						"data": "M2,2 L10,6 L2,10 L6,6 L2,2"
					}
				},
				node:[],
				line:[],
				wordId:0
			}
		},
		created() {
			this.fetchGraphData()
		},
		methods: {
			async fetchGraphData() {
				// 使用uni.request调用后端API
				await this.getId(this.options.param2);
				const id = this.wordId;
				console.log(id);
				uni.request({
					url: 'http://111.231.191.2:8080/api/matrix/process-graph', // 你的API端点
					method: 'GET',
					data: {
						fileIndex: 5, 
						targetNode: id
					},
					success: (response) => {
						if (response.statusCode === 200) {
							console.log(response.data);
							this.node = response.data.nodes;
							this.line = response.data.lines;
							console.log(this.node);
							console.log(this.line);
							this.showGraph()
						} else {
							uni.showToast({
								title: '获取数据失败',
								icon: 'none'
							});
						}
					},
					fail: (error) => {
						console.error('获取graph数据失败:', error);
						uni.showToast({
							title: '请求失败',
							icon: 'none'
						});
					}
				});
			},
			getId(word) {
				 return new Promise((resolve, reject) =>{
					 uni.request({
					 	url: 'http://111.231.191.2:8080/api/getidbyword',
					 	method: 'GET',
					 	data: {
					 		dictionaryId: 5, 
					 		word: word
					 	},
					 	success: (response) => {
							this.wordId = response.data;
					 		resolve('suc');
					 	},
					 	fail: (error) => {
					 		reject('err');
					 	}
					 });
				 });
			},
			showGraph() {
				const jsonData = {
					rootId: '',
					nodes: this.node,
					lines: this.line
				}
				// 以上数据中的node和link可以参考"Node节点"和"Link关系"中的参数进行配置
				this.$refs.graphRef.setJsonData(jsonData, (graphInstance) => {
					// Called when the relation-graph is completed
				})
			},
			onNodeClick(nodeObject, $event) {
				console.log('onNodeClick:', nodeObject.text);
				const action = this.options.param1;
				// 获取对应的word
				const word = nodeObject.text
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

			},
			onLineClick(lineObject, $event) {
				console.log('onLineClick:', lineObject)
			}
		}
	}
</script>
<style>
	.content{
		background-color: #1f2436;
	}
	.title {
		display: flex;
		color: #eedece;
		justify-content: center;
	}
	.title p{
		font-size: 60px;
		font-family: YouYuan;
		font-weight: bold;
	}
	
</style>