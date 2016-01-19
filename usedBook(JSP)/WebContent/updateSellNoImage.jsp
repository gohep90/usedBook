<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="com.oreilly.servlet.MultipartRequest" %> 
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %> 
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%request.setCharacterEncoding("utf-8");	%>
<%
	String savePath ="C:/Users/Min/workspace/usedBook/WebContent/image";
	int sizeLimit =5*1024*1024; //파일업로드 용량제한 10MB
	String fileName="";
	
	String index	="";
    String edt_sbook ="";
    String edt_sauthor ="";
    String edt_spublisher ="";
    String edt_scost ="";
    String edt_sstate ="";
    String edt_sother ="";
    
    System.out.println("qqqqqq");
         
         System.out.println(edt_sbook);
	
		Connection 			conn=null;	
		PreparedStatement 	pstmt=null;
		ResultSet 			rs=null;
		boolean			isSuccess=true;
		String sql="";
		
		try{
			
			String jdbcUrl = "jdbc:mysql://localhost:3306/usedBook";
			String dbId = "owl";
			String dbPass = "owl";
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPass);
			
			 index =request.getParameter("index");
	         edt_sbook =request.getParameter("edt_sbook");
	         edt_sauthor =request.getParameter("edt_sauthor");
	         edt_spublisher =request.getParameter("edt_spublisher");
	         edt_scost =request.getParameter("edt_scost");
	         edt_sstate =request.getParameter("edt_sstate");
	         edt_sother =request.getParameter("edt_sother");
			
			
			pstmt = conn.prepareStatement("update 팝니다 set 책이름='"+edt_sbook+"',저자='"+edt_sauthor+"',출판사='"+edt_spublisher+"',가격='"+edt_scost+"',상태='"+edt_sstate+"',기타='"+edt_sother+"' where 등록번호=" + Integer.parseInt(index));
	          
	          pstmt.executeUpdate();
	          
	          System.out.println("Update Successfully!");
			
		}catch(Exception e){
			e.printStackTrace();
			isSuccess=false;
		}finally{
			if (rs != null)
				try {rs.close();} catch (SQLException sqle) {}
			if (pstmt != null)
				try {pstmt.close();} catch (SQLException sqle) {}
			if (conn != null)
				try {conn.close();} catch (SQLException sqle) {}

		}		
	
%>