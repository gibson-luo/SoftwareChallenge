Vue.prototype.$http = axios

Vue.component('backtotop', {
    template: '#backtotop',
    data: function () {
        return {
            isVisible: false
        };
    },
    methods: {
        initToTopButton: function () {
            $(document).bind('scroll', function () {
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
        backToTop: function () {
            $('html,body').stop().animate({
                scrollTop: 0
            }, 'slow', 'swing');
        }
    },
    mounted: function () {
        this.$nextTick(function () {
            this.initToTopButton();
        });
    }
});

var app = new Vue({
    el: '#app',
    data: {
        jwt: {
            issuedAt: 0,
            issuer: "",
            token: ""
        },
        loggedIn: false,
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
            if (typeof app.pager.is_final_page == 'undefined' || app.pager.is_final_page){
                return;
            }

            app.$http.get('/products', {
                params: {
                    query: app.query,
                    page: app.pager.next_page
                },
                headers: {
                    'Authorization': 'Bearer '.concat(app.jwt.token)
                }
            }).then(function (response) {
                var json = response.data;
                console.log(json)



                if (app.items.length == 0) {
                    app.items = json.result;
                } else {
                    app.items = app.items.concat(json.result);
                }
                app.pager = json.pager;
            }).catch(function (error) {
                console.log(error);
            });
        }

    },

    watch: {
        'pager.is_final_page': function (val, oldVal) {
            app.isShow = val;
        }
    }
})


var signIn = new Vue({
    el: '#signInModal',
    data: {
        username: "",
        password: "",
        isShowUsernameHelper: false,
        isShowPasswordHelper: false
    },
    methods: {

        checkUsername: function () {
            signIn.isShowUsernameHelper = (signIn.username === "");
        },

        checkPassword: function () {
            signIn.isShowPasswordHelper = (signIn.password === "");
        },

        signIn: function () {

            if (signIn.username === "") {
                signIn.isShowUsernameHelper = true;
                return;
            }
            if (signIn.password === "") {
                signIn.isShowPasswordHelper = true;
                return;
            }

            var bodyFormData = new FormData();
            bodyFormData.set('username', signIn.username);
            bodyFormData.set('password', signIn.password);

            app.$http.post('/login', bodyFormData, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {
                var json = response.data;
                if(json.code === 200){
                    $('#signInModal').modal('hide')
                    app.jwt = json.data
                    app.loggedIn = true
                }
            })
        }

    }
})

window.onscroll = () => {
    let bottomOfWindow = document.documentElement.scrollTop + window.innerHeight === document.documentElement.offsetHeight;

    if (bottomOfWindow) {
        app.loadItems();
    }
};