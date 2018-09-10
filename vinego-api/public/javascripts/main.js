(function ($) {
    $(
        function serach() {
            $("#button-go").click(function () {
                $.ajax({
                    url: "/products",
                    data: {
                        query: $("#query").val()
                    }
                }).done(function (data) {


                    if (console && console.log) {
                        console.log("Sample of data:", data);
                    }
                });
            });
        }
    )
})(jQuery)



