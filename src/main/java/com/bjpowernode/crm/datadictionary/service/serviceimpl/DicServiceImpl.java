package com.bjpowernode.crm.datadictionary.service.serviceimpl;

import com.bjpowernode.crm.datadictionary.dao.DicTypeDao;
import com.bjpowernode.crm.datadictionary.dao.DicValueDao;
import com.bjpowernode.crm.datadictionary.domain.DicType;
import com.bjpowernode.crm.datadictionary.domain.DicValue;
import com.bjpowernode.crm.datadictionary.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  21:31
 */
public class DicServiceImpl implements DicService {
    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);

    @Override
    public Map<String, Object> selectDicValueByType() {
        List<DicType> dicTypes = dicTypeDao.selectAllDicType();

        Map<String,Object> map = new HashMap<>();
        for (int i = 0; i < dicTypes.size(); i++) {
            List<DicValue> dicValues = dicValueDao.selectAllDicValue(dicTypes.get(i).getCode());
            map.put(dicTypes.get(i).getCode(),dicValues);
        }

        return map;
    }

}
