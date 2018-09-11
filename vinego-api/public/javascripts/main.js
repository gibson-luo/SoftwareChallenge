Vue.prototype.$http = axios

var app = new Vue({
    el: '#app',
    data: {
        query: "",
        items: []
    },
    methods: {
        loadItems: function () {
            app.$http.get('/products', {
                params: {
                    query: app.query
                }
            }).then(function (response) {
                app.items = response.data.result;
            }).catch(function (error) {
                console.log(error);
            }).then(function () {

            });
        }

    }
})