package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomLoginFilter extends FormAuthenticationFilter {

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        DataReturnVo result = DataReturnVo.businessError(new BusinessException(BusinessErrorEnum.UserNotLogin));
        log.debug(result.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }

}