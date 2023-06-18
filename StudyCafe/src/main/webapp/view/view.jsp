<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<head>
   	<meta charset="UTF-8">
  	<title>글보기</title>
	<link rel="stylesheet" href="../css/bootstrap.css">
	<link rel="stylesheet" href="../css/sidebar.css">
	<link rel="stylesheet" href="../css/list.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script src="../js/bootstrap.js"></script>
	<script src="../js/sidebar.js"></script>

   <script type="text/javascript">
      function backToList(obj){
	    obj.action="${contextPath}/board/listArticles.do";
	    obj.submit();
	  }
   
   </script>
</head>
<body>
<div id="viewport">
		<!--SideBar  -->
		<jsp:include page="sidebar.jsp"></jsp:include>
		<!-- Content -->
		<div id="content">
			<!--Top  -->
			<jsp:include page="top.jsp"></jsp:include>
			<div class="main_back">			
				<div class="container">
					<h1 style="font-family: Namum;">글쓰기</h1>
					<div style="border-bottom: 1px solid #bdbdbd42; margin:5px 20px 20px 20px"></div>
					<br>
					<div class="wrapper" style="text-align:center;margin-top: 10px;">
						<form name="frmArticle" method="post"
							enctype="multipart/form-data">
							<table align="center">
								<tr>
									<td width="150" align="center" bgcolor="#FF9933">글번호</td>
									<td>
										<input type="text" value="${article.articleNO }"disabled />
									 	<input type="hidden" name="articleNO" value="${article.articleNO}" />
									 </td>
								</tr>
								<tr>
									<td width="150" align="center" bgcolor="#FF9933">작성자 아이디</td>
									<td><input type="text" value="${article.id }" name="id" disabled /></td>
								</tr>
								<tr>
									<td width="150" align="center" bgcolor="#FF9933">제목</td>
									<td><input type="text" value="${article.title }" name="title" id="i_title" disabled /></td>
								</tr>
								<tr>
									<td width="150" align="center" bgcolor="#FF9933">내용</td>
									<td>
										<textarea rows="20" cols="60" name="content" id="i_content" disabled >${article.content }</textarea>
									</td>
								</tr>

								<tr>
									<td></td>
								</tr>
								<tr>
									<td width="20%" align="center" bgcolor="#FF9933">등록일자</td>
									<td><input type=text
										value="<fmt:formatDate value="${article.writeDate}" />"
										disabled /></td>
								</tr>
								<tr id="tr_btn_modify">
									<td colspan="2" align="center"><input type=button
										value="수정하기" onClick="fn_modify_article(frmArticle)">
										<input type=button value="취소" onClick="backToList(frmArticle)">
									</td>
								</tr>

								<tr id="tr_btn">
									<td colspan=2 align="center">
									 	<input type=button value="수정하기" onClick="fn_enable(this.form)"> 
									 	<input type=button value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
										<input type=button value="리스트로 돌아가기" onClick="backToList(this.form)"> 
										<input type=button value="답글쓰기" onClick="fn_reply_form('${contextPath}/board/replyForm.do', ${article.articleNO})">
									</td>
								</tr>
							</table>
						</form>

					</div>
					<input type="hidden" name="userId" value="${userId}">
				</div>
			</div>
		</div><!-- Content End  -->
	</div>



</body>
</html>