<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*, java.text.*"  %>
<% request.setCharacterEncoding("utf-8"); %>

<%
   //받은 등록번호에 해당되는 삽니다 목록의 완료여부를 0으로 바꾼다
   
   Connection          	conn = null;
   PreparedStatement    pstmt = null;
   ResultSet          	rs = null;
  		   
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook?useSSL=false", "owl", "owl");
     
	  int a = Integer.parseInt(request.getParameter("num_finish"));
	  	  
	  pstmt = conn.prepareStatement("update 삽니다 set 완료여부 = 0 where 등록번호= " + a);
	  pstmt.executeUpdate();
   }
   
   catch(Exception e){
      e.printStackTrace();
   } 
   
   finally{	      
	      if(pstmt != null) try {pstmt.close();} catch (SQLException ex) {}
	      if(conn != null) try {conn.close();} catch (SQLException ex) {}
	      if(rs != null) try {rs.close();} catch (SQLException ex) {}
   }
         
%>