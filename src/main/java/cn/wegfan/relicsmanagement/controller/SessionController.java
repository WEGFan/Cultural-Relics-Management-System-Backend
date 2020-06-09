package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.model.dto.UserLoginDto;
import cn.wegfan.relicsmanagement.model.stringdto.UserLoginStringDto;
import cn.wegfan.relicsmanagement.model.vo.DataReturnVo;
import cn.wegfan.relicsmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 用户登录
     */
    @PostMapping("")
    public DataReturnVo userLogin(@RequestBody @Valid UserLoginStringDto stringDto) {
        UserLoginDto dto = mapperFacade.map(stringDto, UserLoginDto.class);
        return DataReturnVo.success(userService.userLogin(dto.getWorkId(), dto.getPassword()));
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("")
    public DataReturnVo userLogout() {
        return DataReturnVo.success(userService.userLogout());
    }

}
