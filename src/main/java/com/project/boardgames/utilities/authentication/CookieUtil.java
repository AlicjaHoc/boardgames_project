package com.project.boardgames.utilities.authentication;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void setAuthCookie(HttpServletResponse response, String token) {
        Cookie tokenCookie = new Cookie("jwtToken", token);
        tokenCookie.setPath("/");
        //DEV ONLY
        tokenCookie.setHttpOnly(false);
        tokenCookie.setMaxAge(86400);
        response.addCookie(tokenCookie);
    }
    public static String extractToken(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) { // Replace "jwtToken" with the name of your JWT token cookie
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public static void clearCookie(HttpServletResponse response){
        setAuthCookie(response, null);
    }
}