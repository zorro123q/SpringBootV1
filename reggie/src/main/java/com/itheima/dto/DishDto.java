package com.itheima.dto;

import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

//专门用于接收表单数据 dto
//由于表单数据比较复杂，创建dto一次接收

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;


}
