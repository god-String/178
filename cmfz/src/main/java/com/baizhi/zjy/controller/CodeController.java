package com.baizhi.zjy.controller;


import com.baizhi.zjy.until.ValidateImageCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("code")
public class CodeController {
    @RequestMapping("code")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
        String securityCode = ValidateImageCodeUtils.getSecurityCode();
        session.setAttribute("code",securityCode);
        BufferedImage image = ValidateImageCodeUtils.createImage(securityCode);
        ImageIO.write(image,"png",response.getOutputStream());
    }
}
