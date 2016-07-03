$(document).ready(function () {

    $("#testDatabaseSettings").click(function () {
        $.getJSON("/api/admin/configuration/testDatabase", {
            hostname: $(".hostname").val(),
            port: $(".port").val(),
            username: $(".username").val(),
            password: $(".password").val(),
            database: $(".database").val()
        }).done(function (json) {
            if (!json.success) {
                window.alert("Test failed: " + json.errorMessage);
            } else {
                window.alert("Test successful!");
            }
        }).fail(function (jqxhr, textStatus, error) {
            window.alert("Request Failed: " + textStatus + ", " + error);
        });
    });

});