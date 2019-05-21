package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    //添加线索
    int insertActivityForClue(List<ClueActivityRelation> clueActivityRelations);

    int removeActivityForClue(String id);

    List<ClueActivityRelation> selectClueActivityRelation(String clueId);

    int deleteRelation(String clueId);
}
