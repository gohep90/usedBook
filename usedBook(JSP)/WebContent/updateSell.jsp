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
	
	
	 try{ 
         MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit,
        		 "utf-8",new DefaultFileRenamePolicy()); 
         Enumeration<?> files =multi.getFileNames();
         String file1 =(String)files.nextElement();
         //roomName=multi.getParameter("roomName");
         
         index =URLDecoder.decode(multi.getParameter("index"),"utf-8");;
         edt_sbook =URLDecoder.decode(multi.getParameter("edt_sbook"),"utf-8");;
         edt_sauthor =URLDecoder.decode(multi.getParameter("edt_sauthor"),"utf-8");;
         edt_spublisher =URLDecoder.decode(multi.getParameter("edt_spublisher"),"utf-8");;
         edt_scost =URLDecoder.decode(multi.getParameter("edt_scost"),"utf-8");;
         edt_sstate =URLDecoder.decode(multi.getParameter("edt_sstate"),"utf-8");;
         edt_sother =URLDecoder.decode(multi.getParameter("edt_sother"),"utf-8");;
         
         System.out.println(edt_sbook);
         
         fileName= multi.getFilesystemName(file1); 
         String originFileName = multi.getOriginalFileName(file1);
         
           if(fileName == null) { 
                 System.out.print("파일이 업로드 되지 않았습니다!!"); 
           } else { 
                 System.out.println("getFilesystemName() : " + fileName); 
                 System.out.println("getOriginalFileName() : " + originFileName); 
            } // end if 
     } catch(Exception e) { 
           System.out.println(e.getMessage()); 
     } 
	
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
			
			
			pstmt = conn.prepareStatement("update 팝니다 set 책이름='"+edt_sbook+"',저자='"+edt_sauthor+"',출판사='"+edt_spublisher+"',가격='"+edt_scost+"',상태='"+edt_sstate+"',기타='"+edt_sother+"',사진='"+fileName+"' where 등록번호=" + Integer.parseInt(index));
	    /*      pstmt.setString(1,edt_sbook);
	          pstmt.setString(2,edt_spublisher);
	          pstmt.setString(3,edt_sauthor);
	          pstmt.setString(4,edt_scost);
	          pstmt.setString(5,edt_sstate);
	          pstmt.setString(6,edt_sother);
	          pstmt.setString(7,"gohep90");
	          pstmt.setString(8,fileName);//파일이름 파일 업로드  */
	          
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