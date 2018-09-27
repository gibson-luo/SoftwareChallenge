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


/**
 * the main component
 */
var app = new Vue({
    el: '#app',
    data() {
        return {
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
        }
    },
    methods: {

        search: function () {
            app.items = [];
            app.pager.is_final_page = false;
            app.pager.next_page = undefined;
            app.loadItems();
        },

        loadItems: function () {

            if (!app.loggedIn) {
                app.$refs.signInBtn.click();
                return;
            }

            if (typeof app.pager.is_final_page == 'undefined' || app.pager.is_final_page) {
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
                if (app.items.length == 0) {
                    app.items = json.result;
                } else {
                    app.items = app.items.concat(json.result);
                }
                app.pager = json.pager;
            }).catch(function (error) {
                console.log(error);
            });
        },

        signOut: function () {
            app.$http.get('/logout', {
                params: {
                    username: app.jwt.issuer
                },
                headers: {
                    'Authorization': 'Bearer '.concat(app.jwt.token)
                }
            }).then(function (response) {
                var json = response.data;
                if (json.code == 200) {
                    console.log("sign out success");
                    Object.assign(app.$data, app.$options.data())
                }
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

/**
 * the signIn modal component
 */
var signIn = new Vue({
    el: '#signInModal',
    data() {
        return {
            username: "",
            password: "",
            isShowUsernameHelper: false,
            isShowPasswordHelper: false,
            errorMsg: ""
        }
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

            signIn.$http.post('/login', bodyFormData, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {
                var json = response.data;

                if (json.code === 200) {
                    $('#signInModal').modal('hide')
                    app.jwt = json.data
                    app.loggedIn = true
                    Object.assign(signIn.$data, signIn.$options.data())
                    console.log("sign in success");
                } else {
                    signIn.errorMsg = json.msg;
                }
            }).catch(function (error) {
                console.log(error);
            });
        },

        toSignUp: function () {
            $('#signInModal').modal('hide');
            $('#signUpModal').modal('show');
        }

    }
})

/**
 * the signUp modal component
 */
var signUp = new Vue({
    el: '#signUpModal',
    data() {
        return {
            username: "",
            password: "",
            isShowUsernameHelper: false,
            isShowPasswordHelper: false,
            errorMsg: ""
        }
    },

    methods: {
        checkUsername: function () {
            signUp.isShowUsernameHelper = (signUp.username === "");
        },

        checkPassword: function () {
            signUp.isShowPasswordHelper = (signUp.password === "");
        },

        signUp: function () {

            if (signUp.username === "") {
                signUp.isShowUsernameHelper = true;
                return;
            }
            if (signUp.password === "") {
                signUp.isShowPasswordHelper = true;
                return;
            }

            var bodyFormData = new FormData();
            bodyFormData.set('username', signUp.username);
            bodyFormData.set('password', signUp.password);

            signUp.$http.post('/register', bodyFormData, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (response) {
                var json = response.data;

                if (json.code === 200) {
                    $('#signUpModal').modal('hide')
                    app.jwt = json.data
                    app.loggedIn = true
                    Object.assign(signUp.$data, signUp.$options.data())
                    console.log("sign up success");
                } else {
                    signIn.errorMsg = json.msg;
                }
            }).catch(function (error) {
                console.log(error);
            });
        },

        toSignIn: function () {
            $('#signUpModal').modal('hide');
            $('#signInModal').modal('show');
        }
    }

})

window.onscroll = () => {
    let bottomOfWindow = document.documentElement.scrollTop + window.innerHeight === document.documentElement.offsetHeight;

    if (bottomOfWindow) {
        app.loadItems();
    }
};