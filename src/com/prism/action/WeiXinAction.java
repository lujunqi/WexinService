package com.prism.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.prism.dbutil.VMPreparedStatement;

public class WeiXinAction extends HttpServlet {
	private static final long serialVersionUID = 13L;
	private static final String APPID = "wx91ece5f3cd87008a";
	private static final String APPSECRET = "8e065d6337fee7377b645cc4ecdebeac";

	// private String accesstoken = getAccessToken();
	public void init() throws ServletException {
		System.out.println("WeiXinction loading..............");
		// String[] configLocations = new String[] {"config/baseConfig.xml"};
		// context = new ClassPathXmlApplicationContext(configLocations);
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			req.setCharacterEncoding("UTF-8");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			Enumeration<String> en = req.getParameterNames();
			while (en.hasMoreElements()) {
				String name = en.nextElement();
				System.out.println((name + "==" + req.getParameter(name)));
			}

			String signature = req.getParameter("signature");
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String echostr = req.getParameter("echostr");
			String token = "132456sdsfdfgfdghhf";
			String[] str = { token, timestamp, nonce };
			Arrays.sort(str); // 字典序排序
			String bigStr = str[0] + str[1] + str[2];
			MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
			md.update(bigStr.getBytes());
			byte[] b = md.digest();

			StringBuilder sbDes = new StringBuilder();
			String tmp = null;
			for (int i = 0; i < b.length; i++) {
				tmp = (Integer.toHexString(b[i] & 0xFF));
				if (tmp.length() == 1) {
					sbDes.append("0");
				}
				sbDes.append(tmp);
			}
			String digest = sbDes.toString();
			if (digest.equals(signature)) {
				if (StringUtils.isNotEmpty(echostr)) {
					out.print(echostr);
					return;
				}

				InputStream stream = req.getInputStream();
				Map<String, String> reqMap = parseXMLData(stream);
				String msgType = reqMap.get("MsgType");// 消息类型
				String fromUserName = reqMap.get("FromUserName");

				System.out.println("fromUserName=" + fromUserName);

				if ("text".equals(msgType)) {// 文本消息
					String content = reqMap.get("Content");
					Class.forName("org.sqlite.JDBC");
					Connection conn = DriverManager
							.getConnection("jdbc:sqlite:zieckey.db");
					Statement stat = conn.createStatement();
					try {
						stat.executeUpdate("create table tb_user(name varchar(20), info varchar(2000));");// 创建一个表，两列
					} catch (Exception e) {

					}
					System.out.println(content + "================");
					if (content.startsWith("查 ")) {
						String[] s = content.split(" ");
						if (s.length == 2) {
							String key = s[1];
							
							VMPreparedStatement cmd = new VMPreparedStatement(
									conn);

							String info = slt(cmd, key);
							WeiXinResponse wres = new WeiXinResponse();
							out.write(wres.resText(reqMap, info));

						}

					} else if (content.startsWith("加 ")) {
						String[] s = content.split(" ");
						if (s.length == 3) {
							VMPreparedStatement cmd = new VMPreparedStatement(
									conn);

							String name = s[1];
							String value = s[2];
							String sql1 = "delete from tb_user where name = '"
									+ name + "'";
							cmd.executeUpdate(sql1);
							String sql = "insert into tb_user(name,info)values('"
									+ name + "','" + value + "')";
							cmd.executeUpdate(sql);

							String info = slt(cmd, name);
							WeiXinResponse wres = new WeiXinResponse();
							out.write(wres.resText(reqMap, info));
						}
					} else if (content.startsWith("删 ")) {
						String[] s = content.split(" ");
						if (s.length == 2) {
							VMPreparedStatement cmd = new VMPreparedStatement(
									conn);
							String name = s[1];
							String sql1 = "delete from tb_user where name = '"
									+ name + "' ";
							cmd.executeUpdate(sql1);
							WeiXinResponse wres = new WeiXinResponse();
							out.write(wres.resText(reqMap, "信息已删除"));
						}
						
					} else {
						WeiXinResponse wres = new WeiXinResponse();
						out.write(wres
								.resText(reqMap,
										"查询电话号码时 查 +空格+姓名（查 张三），添加时 加+空格+姓名+空格+电话(加 张三 13907310001 )"));
					}
					conn.close();
				}
				if ("image".equals(msgType)) {// 图片
					String picUrl = reqMap.get("PicUrl");
					download(picUrl, "/home/ljq/prism/pic/" + UUID.randomUUID()
							+ ".jpg");
					WeiXinResponse wres = new WeiXinResponse();
					out.write(wres.resText(reqMap, "图片已经被保存~"));
					System.out.println(picUrl);
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String slt(VMPreparedStatement cmd, String key) throws SQLException {
		String sql = "select name,info from tb_user where name like '%" + key
				+ "%'";
		if("all".equals(key)){
			sql = "select name,info from tb_user ";
		}
		String info = "";
		List<Map<String, Object>> list = cmd.getListColValue(sql, 50);
		for (Map<String, Object> map : list) {
			info += "\n"+map.get("name") + " " + map.get("info") ;
		}
		return info.substring(1);
	}

	private void download(String urlString, String filename) throws Exception {
		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		InputStream is = con.getInputStream();
		byte[] bs = new byte[1024];
		int len;
		OutputStream os = new FileOutputStream(filename);
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		os.close();
		is.close();
	}

	public String iNull(Object v) {
		if (v == null) {
			return "";
		} else {
			return v + "";
		}
	}

	public String decode(String val) {
		String x = val.replaceAll("\\$", "\\%");
		try {
			return URLDecoder.decode(x, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public String encode(String val) {
		try {
			String x = URLEncoder.encode(val, "UTF-8");
			return x.replaceAll("\\%", "\\$");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	private String getUserGroup(String accessToken) {
		return "";
	}

	private String getAccessToken() {// 获取 access token
		String url = String
				.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%1$s&secret=%2$s",
						APPID, APPSECRET);
		JsonUtil ju = new JsonUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> j = (Map<String, Object>) ju.toObject(HttpWeb
				.getGetResponse(url));
		System.out.println(j);
		return j.get("access_token") + "";

	}

	public Map<String, String> parseXMLData(InputStream is) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		SAXReader sr = new SAXReader();
		Document doc = sr.read(is);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		Iterator<Element> it = root.elementIterator();
		Element sonElement = null;

		while (it.hasNext()) {
			sonElement = it.next();
			result.put(sonElement.getName(), sonElement.getText());
		}
		return result;
	}
}
