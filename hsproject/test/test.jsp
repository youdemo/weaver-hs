<%@ page import="weaver.general.Util" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="hsproject.dao.ProjectInfoDao" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%

	ProjectInfoDao pid = new ProjectInfoDao();
	Map<String, String> commonMap = pid.getProjetCommonData("1");
	out.print("aaa"+commonMap.get("procode"));
%>
