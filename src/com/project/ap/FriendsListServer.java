package com.project.ap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.io.InputStream;
import java.util.List;
import java.net.URLEncoder;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


	
public class FriendsListServer extends HttpServlet {
    public static String APP_ID = "1008372449225583";
    public static String APP_SECRET = "e16f80e05ab182532b547db1cc23a6ab";
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String code = request.getParameter("code");

        String RedirectURI = URLEncoder.encode("http://localhost:8080/Project_part2/");
		String MY_ACCESS_TOKEN = "";

		String authURL = "https://graph.facebook.com/oauth/access_token?" +
                "client_id =" + FriendsListServer.APP_ID + "," +
                "redirect_uri =" + RedirectURI + "," +
                "client_secret =" + FriendsListServer.APP_SECRET + "," +
                "code =" + code;

        URL url = new URL(authURL);

		String reading = readURL(url);
		
		String[] partner;
		partner = reading.split(",");

		for (String pair : partner) {

			String[] token = pair.split("=");
			if (token[0].equals("my access token")) {
				MY_ACCESS_TOKEN = token[1]; //1008372449225583|ea3JbHr4hrBWE09hyKiB_FlsOCtokn
			}
		}
		FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN, FriendsListServer.APP_SECRET);
        Connection<User> frnds = null;

		try {
            User user_login = facebookClient.fetchObject("me", User.class);
            
            request.setAttribute("User Login", user_login);
            
            frnds = facebookClient.fetchConnection("/me/friends", User.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<User> List_of_friends = frnds.getData();
		request.setAttribute("FriendList",List_of_friends);

		getServletConfig().getServletContext().getRequestDispatcher("C:\\Users\\Mohini\\workspace\\Project_part2\\WebContent\\FriendsList.jsp").forward(request, response);
	}

	private String readURL(URL url) 
			throws IOException {
		int material;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		InputStream inputstream = url.openStream();
	
		while ((material = inputstream.read()) != -1) {
			stream.write(material);
		}
		return new String(stream.toByteArray());
	}
}
