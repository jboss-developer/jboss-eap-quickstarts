$.ajaxSetup({
	error : function(xhr, textStatus, errorThrown) {
	    alert("Status from server: " + errorThrown);
    }
});

function performUsernamePasswordLogin() {
	var username = $("#userName");
	var password = $("#password");
	
	if (username.is(":valid") && password.is(":valid")) {
		$.ajax({
            url: ("rest/authenticate"),
            type:"POST",
            dataType:"json",
            success: function(response) {
                if (response) {
                    $("#user-message").empty().append("Welcome <b>" + response.user.loginName + "</b> !");
                    $("#authenticatedUserContent").show();
                    $("#loginForms").hide();

                    for (i = 0; i < response.roles.length; i++) {
                        var roleName = response.roles[i].name;

                        if (roleName != 'ADMINISTRATOR') {
                            $("#systemAdministrationLink").text("System Administration is disabled, but you can try to hack and see what happens.");

                            if (roleName == 'DEVELOPER') {
                                $("#risksManagementLink").text("Risks Management is disabled, but you can try to hack and see what happens.");
                            }
                        }
                    }
                } else {
                    $("#login-message").empty().append("Invalid credentials. Please try again.");
                    $("#loginForms").show();
                }
            },
            contentType:"application/x-authc-username-password+json",
            data:JSON.stringify({ userId: username.val(), password: password.val()})
            }
		);
	}
}

function risksManagement() {
    $.ajax({url: ("rest/risksManagement"),
            type:"GET",
            success: function(context) {
                alert(context);
            }}
    );
}

function timesheet() {
    $.ajax({url: ("rest/timesheet"),
            type:"GET",
            success: function(context) {
                alert(context);
            }}
    );
}

function systemAdministration() {
    $.ajax({url: ("rest/systemAdministration"),
            type:"GET",
            success: function(context) {
                alert(context);
            }}
    );
}

function performLogout() {
	$.ajax({url: ("rest/logout"),
		type:"POST",
		dataType:"json",
		contentType:"application/json",
		success: function(context) {
            alert("You're now logged out.");
            window.location = '';
		}}
	);
}

$(document).ready(
    function() {
        $("#authenticatedUserContent").hide();
        $("#login-message").empty().append("");
        $("#user-message").empty().append("");

        $("#usernamePasswordBtn").click(function() {
            performUsernamePasswordLogin();
        });

        $("#logoutBtn").click(function() {
            performLogout();
        });

        $("#risksManagementLink").click(function() {
            risksManagement();
        });

        $("#timesheetLink").click(function() {
            timesheet();
        });

        $("#systemAdministrationLink").click(function() {
            systemAdministration();
        });
    }
);
