<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter" %>
<%@ page import="org.springframework.security.core.AuthenticationException" %>

<div class="span-5">
    <p>Valid username/passwords are:</p>
    <ul>
        <li>keith/melbourne</li>
        <li>erwin/leuven</li>
        <li>jeremy/atlanta</li>
        <li>scott/rochester</li>
    </ul>
</div>

<div class="span-10 append-2 last">
    <c:if test="${not empty param.login_error}">
        <div class="error">
            Your login attempt was not successful, try again.<br/><br/>
            Reason: <%= ((AuthenticationException) session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>
        </div>
    </c:if>
    <form name="f" action="<c:url value="/users/login/authenticate" />" method="post">
        <fieldset>
            <legend>Login Information</legend>
            <p>
                <label for="j_username">User:</label>
                <br/>
                <input type="text" name="j_username" id="j_username"
                       <c:if test="${not empty param.login_error}">value="<%= session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY) %>"
                </c:if> />
                <script type="text/javascript">
                    Spring.addDecoration(new Spring.ElementDecoration({
                        elementId: "j_username",
                        widgetType: "dijit.form.ValidationTextBox",
                        widgetAttrs: { promptMessage: "Your username", required: true }}));
                </script>
            </p>
            <p>
                <label for="j_password">Password:</label>
                <br/>
                <input type="password" name="j_password" id="j_password"/>
                <script type="text/javascript">
                    Spring.addDecoration(new Spring.ElementDecoration({
                        elementId: "j_password",
                        widgetType: "dijit.form.ValidationTextBox",
                        widgetAttrs: { promptMessage: "Your password", required: true}}));
                </script>
            </p>
            <p>
                <input type="checkbox" name="_spring_security_remember_me" id="remember_me"/>
                <label for="remember_me">Don't ask for my password for two weeks:</label>
                <script type="text/javascript">
                    Spring.addDecoration(new Spring.ElementDecoration({
                        elementId: "remember_me",
                        widgetType: "dijit.form.CheckBox"}));
                </script>
            </p>
            <p>
                <button id="submit" type="submit">Login</button>
                <script type="text/javascript">
                    Spring.addDecoration(new Spring.ValidateAllDecoration({event: 'onclick', elementId: 'submit'}));
                </script>
            </p>
        </fieldset>
    </form>
</div>
