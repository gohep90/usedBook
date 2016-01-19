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
      
      String edt_search=request.getParameter("edt_search");
      //String edt_search="";
     
     
      //PreparedStatement 객체 생성 / 쿼리 생성
      
      if(edt_search.equals("")){
    	  pstmt = conn.prepareStatement("select * from 팝니다 where 완료여부 =1"); 
      }else{
    	  pstmt = conn.prepareStatement("select * from 팝니다 where 완료여부 =1 and 책이름 ='"+edt_search+"'"); 
      }
     
      //Query 수행 : recordSet 반환 => Select 문에서 사용
      rs = pstmt.executeQuery();	
      
      JSONObject jsonMain = new JSONObject();
	  JSONArray jArray = new JSONArray();
      
	  
      while(rs.next()){
    	  JSONObject jObject = new JSONObject();

    	  jObject.put("index", rs.getInt("등록번호"));
    	  jObject.put("title", rs.getString("책이름"));
    	  jObject.put("publisher", rs.getString("출판사"));
    	  jObject.put("price", rs.getString("가격"));
    	  jObject.put("photo", rs.getString("사진"));
    	  jObject.put("day", rs.getString("날짜"));
    	  jObject.put("finish", rs.getInt("완료여부"));
    	  
    	  
    	  if(jObject!=null)
				jArray.add(jObject);
      };
		
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