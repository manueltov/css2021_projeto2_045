<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--  No scriptlets!!! 
	  See http://download.oracle.com/javaee/5/tutorial/doc/bnakc.html 
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="model" class="presentation.web.model.NewCustomerModel" scope="request"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="/resources/app.css"> 
<title>SaleSys: criar cliente</title>
</head>
<body>
<h2>Criar Cliente</h2>
<form action="criarCliente" method="post">
    <div class="mandatory_field">
    	<label for="designacao">Designação:</label> 
    	<input type="text" name="designacao" size="50" value="${model.designation}"/> 
    </div>
    <div class="mandatory_field">
		<label for="npc">Número de pessoa colectiva:</label> 
		<input type="text" name="npc" value="${model.VATNumber}"/> <br/>
    </div>
   <div class="optional_field">
   		<label for="telefone">Telefone:</label> 
   		<input type="text" name="telefone" value="${model.phoneNumber}"/>
   </div>
   <div class="optional_field">
   		<label for="dataNascimento">Data de Nascimento:</label> 
   		<input type="date" name="dataNascimento" required pattern="\d{4}-\d{2}-\d{2}"/>
   		<span class ="validity"></span>
   </div>
   <div class="mandatory_field">
		<label for="desconto">Tipo de desconto:</label>
		<select name="desconto">  
			<c:forEach var="desconto" items="${model.discounts}">
				<c:choose>
    				<c:when test="${model.discountType == desconto.id}">
						<option selected = "selected" value="${desconto.id}">${desconto.description}</option>
				    </c:when>
    				<c:otherwise>
						<option value="${desconto.id}">${desconto.description}</option>
				    </c:otherwise>
					</c:choose>
			</c:forEach> 
		</select>
   </div>
   <div class="button" align="right">
   		<input type="submit" value="Criar Cliente">
   </div>
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