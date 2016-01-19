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
	  String my_id=request.getParameter("my_id");

      JSONObject jsonMain = new JSONObject();
      JSONArray jArray = new JSONArray();
      JSONArray jArray2 = new JSONArray();

      int put_num;
      String put_title = "";
      String put_book = "";
      String put_author = "";
      String put_publisher = "";
      String put_cost = "";
      String put_state = "";
      String put_other = "";
      int put_finish;
      
      String put_name="";
      int put_phone;
                        
      pstmt = conn.prepareStatement("select * from 삽니다 where id= '" + my_id + "'and 삭제여부=1");
      rs = pstmt.executeQuery();
       	
      while(rs.next()){
    	  JSONObject jObject = new JSONObject();
    	  
    	  put_num = rs.getInt("등록번호");
    	  put_title = rs.getString("글제목");
    	  put_book = rs.getString("책이름");
    	  put_author = rs.getString("저자");
    	  put_publisher = rs.getString("출판사");
    	  put_cost = rs.getString("가격");
    	  put_state = rs.getString("상태");
    	  put_other = rs.getString("기타");
    	  put_finish = rs.getInt("완료여부");
    	 
    	  jObject.put("put_num", put_num);
    	  jObject.put("put_title", put_title);
    	  jObject.put("put_book", put_book);
    	  jObject.put("put_author", put_author);
    	  jObject.put("put_publisher", put_publisher);
    	  jObject.put("put_cost", put_cost);
    	  jObject.put("put_state", put_state);
    	  jObject.put("put_other", put_other);
    	  jObject.put("put_finish", put_finish);
    	  	  	
          jArray.add(jObject);	
      }

      jsonMain.put("myBuy", jArray);
      
      
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