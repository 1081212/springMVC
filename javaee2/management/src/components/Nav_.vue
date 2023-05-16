<template>
    <a href="#">
        <img :src="resolve_img_url(icon_src)" alt="">
        <div class="data_">
            <p>{{info}}</p>
            <p class="num_">{{num}}</p>
        </div>
    </a>
</template>

<script>
export default {
    name: "Nav_",
    props: {
        icon_src:String,
        info: String,
        num: Number
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
a {
    text-decoration: none;
    display: flex;
}

a img {
    width: 58px;
    height: 58px;
}

a .data_ {
    margin: 0 0 0 18px;
}

a .data_ p {
    margin: 2px 0 0 0;
    font-size: 14px;
    color: #303133;
    font-weight: 400;
}

a .data_ p.num_ {
    margin: 4px 0 0 0;
    font-size: 22px;
    color: #303133;
    font-weight: 600;
}
</style>