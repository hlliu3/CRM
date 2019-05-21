package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ClueDao {

	List<User> selectUserList();
	List<Clue> pageShowClueList(Map<String,Object> map);
	int selectCountClue();
	int insertClue(Clue clue);
	Clue selectClueByClueId(String id);
	List<Activity> selectActivityByClueId(String id);

    Clue selectOriginalClueByClueId(String clueId);

    int deleteClueById(String clueId);
}
