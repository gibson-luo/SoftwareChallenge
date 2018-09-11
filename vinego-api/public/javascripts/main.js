Vue.prototype.$http = axios

Vue.component('backtotop', {
    template: '#backtotop',
    data: function() {
        return {
            isVisible: false
        };
    },
    methods: {
        initToTopButton: function() {
            $(document).bind('scroll', function() {
                var backToTopButton = $('.goTop');
                if ($(document).scrollTop() > 250) {
                    backToTopButton.addClass('isVisible');
                    this.isVisible = true;
                } else {
                    backToTopButton.removeClass('isVisible');
                    this.isVisible = false;
                }
            }.bind(this));
        },
        backToTop: function() {
            $('html,body').stop().animate({
                scrollTop: 0
            }, 'slow', 'swing');
        }
    },
    mounted: function() {
        this.$nextTick(function() {
            this.initToTopButton();
        });
    }
});

var app = new Vue({
    el: '#app',
    data: {
        query: "",
        items: [],
        pager: {
            is_final_page: false,
            next_page: undefined
        },
        isShow: false
    },
    methods: {

        search: function () {
            app.items = [];
            app.pager.is_final_page = false;
            app.pager.next_page = undefined;
            app.loadItems();
        },

        loadItems: function () {
            if (app.pager.is_final_page){
                return;
            }

            app.$http.get('/products', {
                params: {
                    query: app.query,
                    page: app.pager.next_page
                }
            }).then(function (response) {
                var json = response.data;
                if (app.items.length == 0) {
                    app.items = json.result;
                } else {
                    app.items = app.items.concat(json.result);
                }
                app.pager = json.pager;
            }).catch(function (error) {
                console.log(error);
            }).then(function () {

            });
        }

    },

    watch: {
        'pager.is_final_page': function (val, oldVal) {
            app.isShow = val;
        }
    }
})


window.onscroll = () => {
    let bottomOfWindow = document.documentElement.scrollTop + window.innerHeight === document.documentElement.offsetHeight;

    if (bottomOfWindow) {
        app.loadItems();
    }
};