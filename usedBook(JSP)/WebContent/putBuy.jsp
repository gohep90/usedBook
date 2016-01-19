<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="java.util.ArrayList" %>
<% request.setCharacterEncoding("utf-8"); %>
<%
   //////////////////////////////////////////
   // 삭제되지 않은 '삽니다'목록 + 회원 아이디 //
   //////////////////////////////////////////
   
   Connection          	conn = null;
   PreparedStatement    pstmt = null;
   ResultSet          	rs = null;
   
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook?useSSL=false", "owl", "owl");
           
	  JSONObject jsonMain = new JSONObject();
      JSONArray jArray = new JSONArray();
      JSONArray jArray2 = new JSONArray();
      
      String buy_title = "";
      String buy_book = "";
      String buy_author = "";
      String buy_publisher = "";
      String buy_cost = "";
      String buy_state = "";
      String buy_other = "";
      String buy_day = "";
      int buy_tel;
      
      // 삭제되지 않은 '삽니다'목록
      //pstmt = conn.prepareStatement("select * from 삽니다 where 삭제여부=1 and 완료여부=1"); 
      pstmt = conn.prepareStatement("select a.* , b.연락처 from 삽니다 a, 회원 b where a.id=b.id and 삭제여부=1 and 완료여부=1"); 
      rs = pstmt.executeQuery();
      
      while(rs.next()){
    	  JSONObject jObject = new JSONObject();
    	  
    	  buy_title = rs.getString("글제목");
    	  buy_book = rs.getString("책이름");
    	  buy_author = rs.getString("저자");
    	  buy_publisher = rs.getString("출판사");
    	  buy_cost = rs.getString("가격");
    	  buy_state = rs.getString("상태");
    	  buy_other = rs.getString("기타");
    	  buy_day = rs.getString("날짜");
    	  buy_tel = rs.getInt("연락처");
    	  
    	  jObject.put("buy_title", buy_title);
    	  jObject.put("buy_book", buy_book);
    	  jObject.put("buy_author", buy_author);
    	  jObject.put("buy_publisher", buy_publisher);
    	  jObject.put("buy_cost", buy_cost);
    	  jObject.put("buy_state", buy_state);
    	  jObject.put("buy_other", buy_other);
    	  jObject.put("buy_day", buy_day);
    	  jObject.put("buy_tel", buy_tel);
      	  	  
          jArray.add(jObject);
      }
      
      jsonMain.put("Buy", jArray);
      
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