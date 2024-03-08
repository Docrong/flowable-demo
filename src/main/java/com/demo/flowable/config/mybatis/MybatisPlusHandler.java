package com.demo.flowable.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author : gr
 * @date : 2024/3/7 9:12
 */
@Component
public class MybatisPlusHandler implements MetaObjectHandler {
    
    //新增触发事件
    @Override
    public void insertFill(MetaObject metaObject) {
        
    }

    //修改触发事件
    @Override
    public void updateFill(MetaObject metaObject) {

    }
    
}
