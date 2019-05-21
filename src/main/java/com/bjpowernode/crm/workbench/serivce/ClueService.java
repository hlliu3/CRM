package com.bjpowernode.crm.workbench.serivce;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;

import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  22:39
 */
public interface ClueService {
    List<User> selectUserList();

    boolean insertClue(Clue clue);

    PageDataVO<Clue> pageShowClueList(Map<String,Object> map);

    Clue selectClueByClueId(String id);

    List<Activity> selectActivityByClueId(String id);

    int insertActivityForClue(List<ClueActivityRelation>clueActivityRelations);

    boolean removeActivityForClue(String id);

    boolean clueConver(String clueId, Tran tran, String createBy);
}
