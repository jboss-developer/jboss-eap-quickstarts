$.ajaxSetup({
    url: ("rest/authenticate"),
    type:"POST",
    dataType:"json",
    success: function(user) {
        if (user) {
            $("#message").empty().append("Welcome " + user.loginName + " !");
            $("#logoutBtn").show();
            $("#loginForms").hide();
        } else {
            $("#message").empty().append("Invalid credentials. Please try again.");
            $("#loginForms").show();
        }
    },
	error : function(xhr, textStatus, errorThrown) {
	    alert("Could not authenticate.")
    }
});

function performUsernamePasswordLogin() {
	var username = $("#userName");
	var password = $("#password");
	
	if (username.is(":valid") && password.is(":valid")) {
		$.ajax({
            contentType:"application/x-authc-username-password+json",
            data:JSON.stringify({ userId: username.val(), password: password.val()})
            }
		);
	}
}

function performTokenBasedLogin() {
    var token = $("#token");

    if (token.is(":valid")) {
        $.ajax({
            contentType:"application/x-authc-token",
            data: token.val()
            }
        );
    }
}

function performLogout() {
	$.ajax({url: ("rest/logout"),
		type:"POST",
		dataType:"json",
		contentType:"application/json",
		success: function(context) {
            alert("You're now logged out.");
            $("#loginForms").show();
            $("#logoutBtn").hide();
            $("#message").empty().append("");
		}}
	);
}

$(document).ready(
    function() {
        $("#logoutBtn").hide();

        $("#usernamePasswordBtn").click(function() {
            performUsernamePasswordLogin();
        });

        $("#tokenBtn").click(function() {
            performTokenBasedLogin();
        });

        $("#logoutBtn").click(function() {
            performLogout();
        });
    }
);
