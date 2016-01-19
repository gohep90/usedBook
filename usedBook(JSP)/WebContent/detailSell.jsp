<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.json.simple.*" %>
<%@ page import="java.util.ArrayList" %>
<% request.setCharacterEncoding("utf-8"); %>
<%
   Connection          	conn = null;   
   PreparedStatement    pstmt = null;
   ResultSet          	rs = null;
   
   try{
      //MYSQL 드라이버 로딩
      Class.forName("com.mysql.jdbc.Driver");      
      //Connection 객체 생성
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook", "owl", "owl");
         
      String reindex = request.getParameter("index");
      int index = Integer.parseInt(reindex);
      //PreparedStatement 객체 생성 / 쿼리 생성
      pstmt = conn.prepareStatement("select * from 팝니다 where 등록번호 ="+index); 
      //Query 수행 : recordSet 반환 => Select 문에서 사용
      rs = pstmt.executeQuery();	
      
      JSONObject jsonMain = new JSONObject();
	  JSONArray jArray = new JSONArray();
	  JSONObject jObject = new JSONObject();
      
	  
      while(rs.next()){
    	  jObject.put("info_day", rs.getString("날짜"));
    	  jObject.put("info_book", rs.getString("책이름"));
    	  jObject.put("info_publisher", rs.getString("출판사"));
    	  jObject.put("info_author", rs.getString("저자"));
    	  jObject.put("info_cost", rs.getString("가격"));
    	  jObject.put("photo", rs.getString("사진"));
    	  jObject.put("info_state", rs.getString("상태"));
    	  jObject.put("info_other", rs.getString("기타"));
    	  
      };
		
      jArray.add(jObject);
	        // 최종적으로 배열을 하나로 묶음
		jsonMain.put("List", jArray);
		
	        // 안드로이드에 보낼 데이터를 출력
		out.println(jsonMain.toJSONString());
      
   }
   
   catch(Exception e){
      e.printStackTrace();
   } finally{	      
	      if(pstmt != null) try {pstmt.close();} catch (SQLException ex) {}
	      if(conn != null) try {conn.close();} catch (SQLException ex) {}
	      if(rs != null) try {rs.close();} catch (SQLException ex) {}
	 }
         
%>