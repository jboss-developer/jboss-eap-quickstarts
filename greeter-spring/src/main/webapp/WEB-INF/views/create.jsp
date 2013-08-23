<%--
    JBoss, Home of Professional Open Source
    Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
    contributors by the @authors tag. See the copyright.txt in the
    distribution for a full listing of individual contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hello</title>
</head>
<body>
<form method="post" action="create">
    <table>
        <tr>
            <td>Enter username:</td>
            <td><input type="text" name="username" value=""/></td>
        </tr>
        <tr>
            <td>Enter first name:</td>
            <td><input type="text" name="firstName" value=""/></td>
        </tr>
        <tr>
           <td> Enter last name: </td>
            <td><input type="text" name="lastName" value=""/></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value="Add User"></td>
        </tr>
        <tr><td colspan="2" align="center">
            <c:if test="${!empty message}">
            <c:out value="${message}"/>
            </c:if></td></tr> 
        <tr><td colspan="2" align="center"><a href="greet">Greet a user!</a></td></tr>
    </table>
</form>

</body>
</html>