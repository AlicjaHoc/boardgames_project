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
        tokenCookie.setSecure(false);
        tokenCookie.setMaxAge(86400);

        // Add SameSite=None to cookie
        String cookieHeaderValue = tokenCookie.getName() + "=" + tokenCookie.getValue() + "; SameSite=None";
        if(tokenCookie.getMaxAge() > 0) {
            cookieHeaderValue += "; Max-Age=" + tokenCookie.getMaxAge();
        }
        if(tokenCookie.getSecure()) {
            cookieHeaderValue += "; Secure";
        }
        if(tokenCookie.isHttpOnly()) {
            cookieHeaderValue += "; HttpOnly";
        }
        if(tokenCookie.getPath() != null) {
            cookieHeaderValue += "; Path=" + tokenCookie.getPath();
        }
        response.setHeader("Set-Cookie", cookieHeaderValue);
    }
    public static String extractToken(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: " + cookie.getName());
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