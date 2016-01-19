<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*, java.text.*"  %>
<% request.setCharacterEncoding("utf-8"); %>

<%
   Connection          	conn = null;
   PreparedStatement    pstmt = null;
   ResultSet          	rs = null;
  		   
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook?useSSL=false", "owl", "owl");
     
	  int num = Integer.parseInt(request.getParameter("num"));
      String btitle = request.getParameter("edt_btitle");
      String bbook = request.getParameter("edt_bbook");
      String bauthor = request.getParameter("edt_bauthor");
      String bpublisher = request.getParameter("edt_bpublisher");	
      String bcost = request.getParameter("edt_bcost");
      String bstate = request.getParameter("edt_bstate");
      String bother = request.getParameter("edt_bother");
      
      pstmt = conn.prepareStatement("update 삽니다 set 글제목='"+btitle+"',책이름='"+bbook+"',저자='"+bauthor+"',출판사='"+bpublisher+"',가격='"+bcost+"',상태='"+bstate+"',기타='"+bother+"' where 등록번호= " + num);
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