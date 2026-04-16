<template>
	<view class="text">
		<text class="title">制作人名单</text>
		<table class="uni-table">
			<tr>
				<th>职位</th>
				<th>网名</th>
				<th>邮箱</th>
			</tr>
			<tr>
				<td>前端</td>
				<td>是卜凡呀</td>
				<td>595937548@qq.com</td>
			</tr>
			<tr>
				<td>后端</td>
				<td>云笙</td>
				<td>cloudyun233@outlook.com</td>
			</tr>
			<tr>
				<td>算法</td>
				<td>ToHatoss</td>
				<td>painn183@outlook.com</td>
			</tr>
			<tr>
				<td>审核</td>
				<td>雨宫莲</td>
				<td>854253851@qq.com</td>
			</tr>
		</table>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style>
	.text {
		text-align: center;
		background-color: #1f2436;
		height: 95vh;
	}
		
	.title{
		font-size: 50px;
		color: #eedece;
		font-family: YouYuan;
		font-weight: bold;
	}
	.uni-table {
		border-collapse: collapse;
		margin-right: auto;
		margin-left: auto;
		margin-top: 20px;
	}
	
	.uni-table th,
	.uni-table td {
		border: 0 solid #0d101b;
		padding: 8px;
		text-align: center;
		color: #eedece;
		font-size: 30px;
		font-family: YouYuan;
		font-weight: bold;
	}
	@media (max-width: 768px) {
		.uni-table {
			width: 100%;
		}
		.uni-table th,
		.uni-table td{
			font-size: 18px;
		}
	
	}
</style>