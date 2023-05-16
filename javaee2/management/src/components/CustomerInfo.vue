<template>
	<table>
		<tr>
			<td class="key_">客户账号</td>
			<td>{{info.account}}</td>
			<td class="key_">姓名</td>
			<td>{{info.name}}</td>
		</tr>
		<tr>
			<td class="key_">性别</td>
			<td>{{info.gender}}</td>
			<td class="key_">手机号码</td>
			<td>{{info.number}}</td>
		</tr>
		<tr>
			<td class="key_">证件类型</td>
			<td>{{info.id_type}}</td>
			<td class="key_">证件号码</td>
			<td>{{info.id_}}</td>
		</tr>
		<tr>
			<td class="key_">出生日期</td>
			<td>{{info.birth_date}}</td>
			<td class="key_">实名认证</td>
			<td class="green_">{{info.verified}}</td>
		</tr>
		<tr>
			<td class="key_">风险等级</td>
			<td>{{info.risk_level}}</td>
			<td class="key_">风险有效期</td>
			<td>{{info.risk_date}}</td>
		</tr>
		<tr>
			<td class="key_">客户级别</td>
			<td>
				<button class="btn_">
					{{info.customer_level}}
				</button>
			</td>
			<td class="key_">联网核查</td>
			<td>{{info.net_verified}}</td>
		</tr>
	</table>
</template>

<script>
export default {
	name: "CustomerInfo",
	props: {
		info: {
			account: String,
			name: String,
			gender: String,
			number: String,
			id_type: String,
			id_: String,
			birth_date: String,
			verified: String,
			risk_level: String,
			risk_date: String,
			customer_level:String,
			net_verified: String,

		}
	}


}
</script>

<style scoped>
table {
	display: block;
	border-collapse: collapse;
}

table tr {
	height: 40px;
}

table tr td {
	width: 200px;
	height: 40px;
	border: 1px solid #ddd;
	font-size: 14px;
	color: #303133;
	font-weight: 400;
	padding: 0 0 0 15px;
}

table tr td.key_ {
	font-size: 14px;
	color: #303133;
	font-weight: 600;
	background-color: #F7FBFC;
}

table tr td.green_{
	color:#2CD681;
}

table tr td .btn_{
	color:#1FC7ED;
	display: block;
	background-color: white;
	border: 1px solid #ddd;
	padding:4px 8px;
	border-radius: 4px;
}
</style>