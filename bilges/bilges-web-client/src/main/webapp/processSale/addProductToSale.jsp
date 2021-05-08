<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--  No scriptlets!!! 
	  See http://download.oracle.com/javaee/5/tutorial/doc/bnakc.html 
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="model"
	class="presentation.web.model.AddProductToSaleModel" scope="request" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="/resources/app.css">
<title>SaleSys: efectuar venda</title>
</head>
<body>
	<h2>Juntar produtos à venda</h2>
	<p>Número da venda: ${model.sale}</p>
	<p>Cliente: ${model.customer}</p>
	<p>Total: ${model.total}</p>
	<form action="adicionarProdutoVenda" method="post">
		<input id="venda" name="venda" type="hidden" value="${model.sale}">
		<fieldset>
			<legend>Adicionar Produto</legend>
			<div class="mandatory_field">
				<label for="produto">Código do produto:</label> <input type="text"
					name="produto" value="${model.product}" /> <br />
			</div>
			<div class="mandatory_field">
				<label for="quantidade">Quantidade:</label> <input type="text"
					name="quantidade" value="${model.quantity}" /> <br />
			</div>
			<div class="button" align="right">
				<input type="submit" value="Adicionar">
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