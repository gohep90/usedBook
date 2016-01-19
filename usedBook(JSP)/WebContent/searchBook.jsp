<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="java.util.ArrayList" %>
<% request.setCharacterEncoding("utf-8"); %>
<%
   Connection          	conn = null;
   PreparedStatement    pstmt = null;
   ResultSet          	rs = null;

   try{
      Class.forName("com.mysql.jdbc.Driver");
	  conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook?useSSL=false", "owl", "owl");      
      String book = request.getParameter("edt_search");
      //String book = "구구";

      JSONObject jsonMain = new JSONObject();
      JSONArray jArray = new JSONArray();
      
      String put_title = "";
      String put_book = "";
      String put_author = "";
      String put_publisher = "";
      String put_cost = "";
      String put_state = "";
      String put_other = "";
      String put_day = "";
      int put_tel;
                        
      //pstmt = conn.prepareStatement("select * from 삽니다 where 책이름 = '" + book + "'and 삭제여부=1");
      pstmt = conn.prepareStatement("select a.* , b.연락처 from 삽니다 a, 회원 b where a.id=b.id and 삭제여부=1 and 완료여부=1 and 책이름 = '"+ book +"'"); 
      rs = pstmt.executeQuery();
       	
      while(rs.next()){
    	  JSONObject jObject = new JSONObject();
    	  
    	  put_title = rs.getString("글제목");
    	  put_book = rs.getString("책이름");
    	  put_author = rs.getString("저자");
    	  put_publisher = rs.getString("출판사");
    	  put_cost = rs.getString("가격");
    	  put_state = rs.getString("상태");
    	  put_other = rs.getString("기타");
    	  put_day = rs.getString("날짜");
    	  put_tel = rs.getInt("연락처");
    	 
    	  jObject.put("put_title", put_title);
    	  jObject.put("put_book", put_book);
    	  jObject.put("put_author", put_author);
    	  jObject.put("put_publisher", put_publisher);
    	  jObject.put("put_cost", put_cost);
    	  jObject.put("put_state", put_state);
    	  jObject.put("put_other", put_other);
    	  jObject.put("put_day", put_day);
    	  jObject.put("put_tel", put_tel);
          	  	
          jArray.add(jObject);
      }
		
      jsonMain.put("sBuy", jArray);
      
      out.println(jsonMain.toJSONString());
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