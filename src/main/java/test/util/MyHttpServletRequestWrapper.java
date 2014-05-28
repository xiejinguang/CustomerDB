/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.util;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author 谢金光
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper{

    @Override
    public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
        super.setCharacterEncoding("UTF-8"); //To change body of generated methods, choose Tools | Templates.
    }

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
}
