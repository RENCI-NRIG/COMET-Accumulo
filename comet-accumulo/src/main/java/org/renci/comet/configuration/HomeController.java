package org.renci.comet.configuration;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Home redirection to swagger api documentation 
 */

//@RequestMapping(value = "/authenticateUser", method = RequestMethod.GET)
//public Map<String, String> authenticateUser(HttpServletRequest request,
//        HttpServletResponse response, String userPassword) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException 


@Controller
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request,
            HttpServletResponse response, String userPassword) {
    		X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
		if (certs == null) {
			System.out.print("Cert is NULL!!!\n");
		} else {
			
			for (X509Certificate x : certs) {
				System.out.println(x);
			}
		}
		
        System.out.println("swagger-ui.html");
        return "redirect:swagger-ui.html";
    }
}
