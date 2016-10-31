<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 26.10.2016
  Time: 13:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link rel="shortcut icon" href="<c:url value="/resources/image/icon.jpg"/>">
  <link href="<c:url value="/resources/css/style.css"/>" type="text/css" rel="stylesheet">
  <title>Крестики-Нолики</title>
</head>
<body>

  <%--<script>
    alert("Вы сделали ход в заполненной клетке!");
  </script>--%>

  ${newMessage}
  <form action="/move" method="post">
    <div class="containerForButton">
      <div id="1">
        <button class="1" name="but" value="11" type="submit"><img src="<c:url value="/resources/image/${image11}.jpg"/>"></button>
        <button class="2" name="but" value="12" type="submit"><img src="<c:url value="/resources/image/${image12}.jpg"/>"></button>
        <button class="3" name="but" value="13" type="submit"><img src="<c:url value="/resources/image/${image13}.jpg"/>"></button>
      </div>
      <div id="2">
        <button class="1" name="but" value="21" type="submit"><img src="<c:url value="/resources/image/${image21}.jpg"/>"></button>
        <button class="2" name="but" value="22" type="submit"><img src="<c:url value="/resources/image/${image22}.jpg"/>"></button>
        <button class="3" name="but" value="23" type="submit"><img src="<c:url value="/resources/image/${image23}.jpg"/>"></button>
      </div>
      <div id="3">
        <button class="1" name="but" value="31" type="submit"><img src="<c:url value="/resources/image/${image31}.jpg"/>"></button>
        <button class="2" name="but" value="32" type="submit"><img src="<c:url value="/resources/image/${image32}.jpg"/>"></button>
        <button class="3" name="but" value="33" type="submit"><img src="<c:url value="/resources/image/${image33}.jpg"/>"></button>
      </div>
    </div>
    <%--<h2>${image11}</h2>--%>
    <input type="hidden" name="11" value="${image11}"/><input type="hidden" name="12" value="${image12}"/><input type="hidden" name="13" value="${image13}"/>
    <input type="hidden" name="21" value="${image21}"/><input type="hidden" name="22" value="${image22}"/><input type="hidden" name="23" value="${image23}"/>
    <input type="hidden" name="31" value="${image31}"/><input type="hidden" name="32" value="${image32}"/><input type="hidden" name="33" value="${image33}"/>
  </form>
  <form action="/" method="GET">
    <div style="margin-left: 45.6%; margin-top: 5%">
      <button class="RestartButton">
        <img src="<c:url value="/resources/image/restart.jpg"/>" style="width:100px; height:100px; border-radius:10px;">
      </button>
    </div>
  </form>
  <div class="userSteps"><h1>User Steps</h1>${movesUserModel}</div>
  <div class="computerSteps"><h1>Computer Steps</h1>${movesCompModel}</div>
</body>
</html>
