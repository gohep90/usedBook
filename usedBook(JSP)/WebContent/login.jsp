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
	    
		String dbID = request.getParameter("ID");
		String dbPW = request.getParameter("PW");
		
		
		String dbname="";
		String dbtel="";
		String dbbirth="";
		
	 	System.out.println(dbID);
	 	System.out.println(dbPW);
	 	
	 	pstmt = conn.prepareStatement("select 이름,연락처,생일 from 회원 where ID = ? and PW = ?");
	    pstmt.setString(1,dbID);
	    pstmt.setString(2,dbPW);
	    
	    rs=pstmt.executeQuery();
	    
	    if(rs.next()){
	    //	result=true;
	    	System.out.println("로그인요");
	    	dbname=rs.getString("이름");
	    	dbtel=rs.getString("연락처");
	    	dbbirth=rs.getString("생일");
	    	
	    	System.out.println(dbname);	//찾은 값 출력 해주기
	    	session.setAttribute("ID",dbID);//세션 세팅
	    	System.out.println("세션 생성완료");	
	    }else{
	    	System.out.println("실패");
	    }
	    
	    JSONObject jsonMain = new JSONObject();
		JSONArray jArray = new JSONArray();
		JSONObject jObject = new JSONObject();

		        // 안드로이드로 보낼 메시지를 만듬
		
		jObject.put("name", dbname);
		jObject.put("tel", dbtel);
		jObject.put("birth", dbbirth);
		
		       // 위에서 만든 각각의 객체를 하나의 배열 형태로 만듬
		jArray.add(jObject);
			
		        // 최종적으로 배열을 하나로 묶음
		jsonMain.put("NAME", jArray);	
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