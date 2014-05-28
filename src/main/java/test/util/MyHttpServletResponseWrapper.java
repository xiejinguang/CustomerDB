/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.util;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author 谢金光
 */
public class MyHttpServletResponseWrapper extends HttpServletResponseWrapper{

 

    public MyHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }
       @Override
    public void setHeader(String name, String value) {        
        super.setHeader(name, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setContentType(String type) {
        super.setContentType(type); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCharacterEncoding(String charset) {
        
        super.setCharacterEncoding("UTF-8"); //To change body of generated methods, choose Tools | Templates.
    }
    
}
