<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="org.json.*" %>
<%@ page import="org.json.simple.*" %>
<%@ page import="java.util.ArrayList" %>
<% request.setCharacterEncoding("utf-8"); %>
<%

	Connection          	conn = null;
	PreparedStatement    pstmt = null;
	ResultSet          	rs = null;

	try{
		//boolean result = false;
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usedBook", "owl", "owl");
	    
	    String dbname = request.getParameter("name");
		String dbtel = request.getParameter("tel");
		String dbID = "";
		String result = "실패";
		
		
		System.out.println(dbID);
	 	System.out.println(dbtel);
	 	
	 	
	 	pstmt = conn.prepareStatement("select ID from 회원 where 이름 = ? and 연락처 = ?");
	    pstmt.setString(1,dbname);
	    pstmt.setString(2,dbtel);
	    
	    rs=pstmt.executeQuery();
	    
	    if(rs.next()){
	    	dbID=rs.getString("ID");
		    result = "성공";
		    System.out.println("dbID" + result);
		    
		    /*
	    	pstmt = conn.prepareStatement("UPDATE 회원 SET PW = ? where ID = ?");
	    	pstmt.setString(1,dbnewPW);
	    	result = "성공";
		    System.out.println(result);
		    */
		}else{
			System.out.println(result);
		}
	    
	    JSONObject jsonMain = new JSONObject();
		JSONArray jArray = new JSONArray();
		JSONObject jObject = new JSONObject();

		        // 안드로이드로 보낼 메시지를 만듬
		
		jObject.put("id", dbID);
		
		       // 위에서 만든 각각의 객체를 하나의 배열 형태로 만듬
		jArray.add(jObject);
			
		        // 최종적으로 배열을 하나로 묶음
		jsonMain.put("ID", jArray);	
		        // 안드로이드에 보낼 데이터를 출력
		System.out.println("테스트" + jsonMain.toJSONString());
		out.println(jsonMain.toJSONString());

	    
	}catch(Exception e){
	    e.printStackTrace();
	}finally{	      
		if(pstmt != null) try {pstmt.close();} catch (SQLException ex) {}
		if(conn != null) try {conn.close();} catch (SQLException ex) {}
		if(rs != null) try {rs.close();} catch (SQLException ex) {}
	}
         
%>