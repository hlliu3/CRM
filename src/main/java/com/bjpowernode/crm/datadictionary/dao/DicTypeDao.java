package com.bjpowernode.crm.datadictionary.dao;

import com.bjpowernode.crm.datadictionary.domain.DicType;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  21:23
 */
public interface DicTypeDao {
    List<DicType> selectAllDicType();
}
