<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--  No scriptlets!!! 
	  See http://download.oracle.com/javaee/5/tutorial/doc/bnakc.html 
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="model" class="presentation.web.model.NewSaleModel" scope="request"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="/resources/app.css"> 
<title>SaleSys: efectuar venda</title>
</head>
<body>
<h2>Criar venda</h2>
<form action="criarVenda" method="post">
	<fieldset>
		<legend>Venda</legend>
	    <div class="mandatory_field">
			<label for="npc">Número de pessoa colectiva:</label> 
			<input type="text" name="npc" value="${model.VATNumber}"/> <br/>
   		</div>
    	<div class="button" align="right">
   			<input type="submit" value="Criar Venda">
   		</div>
   </fieldset>
</form>
<c:if test="${model.hasMessages}">
	<p>Mensagens</p>
	<ul>
	<c:forEach var="mensagem" items="${model.messages}">
		<li>${mensagem} 
	</c:forEach>
	</ul>
</c:if>
</body>
</html>