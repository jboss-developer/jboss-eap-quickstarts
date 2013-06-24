<%--
  Created by IntelliJ IDEA.
  User: tmehta
  Date: 21/06/13
  Time: 3:46 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>

<head>
    <title>Grails Starter Application</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'screen.css')}"/>
</head>

<body>
<div id="container">
    <div class="dualbrand">
        <img src="${resource(dir: 'gfx', file: 'dualbrand_logo.png')}"/>
    </div>

    <div id="content">
        <h1>Welcome to JBoss!</h1>

        <div>
            <p>You have successfully deployed a basic Grails web application.</p>

            <h3>Your application can run on:</h3>
            <img src="${resource(dir: 'gfx', file: 'dualbrand_as7eap.png')}"/>
        </div>

        <g:form url="[controller:'member', action:'member']" style="width: auto">
            <h2>Member Registration</h2>

            <p>Enforces annotation-based constraints defined on the model class.</p>
            <table>
                <tbody>
                <tr>
                    <td><label>Name: </label></td>
                    <td><g:textField name="name" value="${newMember?.name}"/></td>
                    <td><span class="invalid"><g:renderErrors bean="${newMember}" as="list" field="name"/></span></td>
                </tr>
                <tr>
                    <td><label>Email: </label></td>
                    <td><g:textField name="email" value="${newMember?.email}"/></td>
                    <td><span class="invalid"><g:renderErrors bean="${newMember}" as="list" field="email"/></span></td>
                </tr>
                <tr>
                    <td><label>Phone Number: </label></td>
                    <td><g:textField name="phoneNumber" value="${newMember?.phone_number}"/></td>
                    <td><span class="invalid"><g:renderErrors bean="${newMember}" as="list" field="phone_number" class="invalid"/></span></td>
                </tr>
                </tbody>
            </table>
            <table>
                <tr>
                    <td>
                        <input type="submit" value="Register" class="register"/>
                        <input type="reset" value="Cancel" class="cancel"/>
                    </td>
                </tr>
            </table>
        </g:form>
        <h2>Members</h2>
        <g:if test="${members.size() == 0}">
            <em>No registered members.</em>
        </g:if>
        <g:else>
            <table class="simpletablestyle">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone #</th>
                    <th>REST URL</th>
                </tr>
                </thead>
                <tbody>
                <g:each var="member" in="${members}">
                    <tr>
                        <td>${member.id}</td>
                        <td>${member.name}</td>
                        <td>${member.email}</td>
                        <td>${member.phone_number}</td>
                        <td><a href="${request.contextPath}/rest/member/${member.id}">/rest/member/${member.id}</a></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <table class="simpletablestyle">
                <tr>
                    <td>
                        REST URL for all members: <a href="${request.contextPath}/rest/members">/rest/members</a>
                    </td>
                </tr>
            </table>
        </g:else>
    </div>

    <div id="aside">
        <p>Learn more about JBoss Enterprise Application Platform 6.</p>
        <ul>
            <li><a
                    href="http://red.ht/jbeap-6-docs">Documentation</a></li>
            <li><a href="http://red.ht/jbeap-6">Product Information</a></li>
        </ul>

        <p>Learn more about JBoss AS 7.</p>
        <ul>
            <li><a
                    href="https://docs.jboss.org/author/display/AS7/Getting+Started+Developing+Applications+Guide">Getting
                Started Developing Applications Guide</a></li>
            <li><a href="http://jboss.org/jbossas">Community Project Information</a></li>
        </ul>
    </div>

    <div id="footer">
    </div>
</div>
</body>
</html>
