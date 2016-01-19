<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.*, java.text.*"  %>
<% request.setCharacterEncoding("utf-8"); %>

<%
   Connection          	conn = null;
   PreparedStatement    pstmt = null;
   ResultSet          	rs = null;
  
   // 현재 날짜 구하기 yyyymmdd
   java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
   String day = formatter.format(new java.util.Date());
		   
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook?useSSL=false", "owl", "owl");
                  
      String btitle = request.getParameter("edt_btitle");
      String bbook = request.getParameter("edt_bbook");
      String bauthor = request.getParameter("edt_bauthor");
      String bpublisher = request.getParameter("edt_bpublisher");	
      String bcost = request.getParameter("edt_bcost");
      String bstate = request.getParameter("edt_bstate");
      String bother = request.getParameter("edt_bother");  
      String id = request.getParameter("edt_id");  

 	  //삽니다 게시판 목록을 '삽니다'디비에 저장
      pstmt = conn.prepareStatement("insert into 삽니다(글제목, 책이름, 저자, 출판사, 가격, 상태, 기타, ID, 날짜) values(?,?,?,?,?,?,?,?,?)");
      pstmt.setString(1, btitle);
      pstmt.setString(2, bbook);
      pstmt.setString(3, bauthor);
      pstmt.setString(4, bpublisher);
      pstmt.setString(5, bcost);
      pstmt.setString(6, bstate);
      pstmt.setString(7, bother);
      pstmt.setString(8, id);
      pstmt.setString(9, day);
      
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