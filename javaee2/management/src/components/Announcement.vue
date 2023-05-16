<template>
    <div>
        <div class="row">
            <img :src="resolve_img_url(icon_src)" alt="">
            <p>{{title}}</p>
            <p class="date_">{{date}}</p>
        </div>
    </div>

</template>

<script>

export default {
    name: 'Announcement',
    props: {
        icon_src: String,
        title: String,
        date: String
    },
    methods: {
        resolve_img_url: function (path) {
            let images = require.context('../assets/', false, /\.png$|\.jpg$/)
            return images("./" + path)
        }
    }

}
</script>

<style scoped>
div {
    width: 100%;
    position: relative;
}

div .row {
    display: flex;
    flex:33%;
}

div .row img{
    width:30px;
    height: 30px;
    margin:0 26px 0 15px;
}

div .row p{
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    line-height: 30px;
    height: 30px;
    width:144px;
    font-size: 14px;
    color: #303133;
	font-weight: 400;
}

div .row p:nth-of-type(1){
    margin:0 100px 0 0;
}

div .row p.date_ {
    text-align: right;
    justify-self: end;
}
</style>