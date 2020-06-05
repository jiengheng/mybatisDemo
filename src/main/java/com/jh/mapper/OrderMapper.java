package com.jh.mapper;

import java.util.Map;

public interface OrderMapper {

    Map<String, Object> selectOrderByid(Integer id);
}
