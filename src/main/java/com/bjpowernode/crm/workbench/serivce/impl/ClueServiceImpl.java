package com.bjpowernode.crm.workbench.serivce.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.serivce.ClueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  22:39
 */
public class ClueServiceImpl implements ClueService {
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public List<User> selectUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }

    @Override
    public boolean insertClue(Clue clue) {
        boolean flag = true;

        if (clueDao.insertClue(clue) != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PageDataVO<Clue> pageShowClueList(Map<String, Object> map) {
        int total = clueDao.selectCountClue();

        List<Clue> clues = clueDao.pageShowClueList(map);
        PageDataVO pageDataVO = new PageDataVO();
        pageDataVO.setDatalist(clues);
        pageDataVO.setTotal(total);

        return pageDataVO;
    }

    @Override
    public Clue selectClueByClueId(String id) {
        Clue clue = clueDao.selectClueByClueId(id);
        return clue;
    }

    @Override
    public List<Activity> selectActivityByClueId(String id) {
        List<Activity> activities = clueDao.selectActivityByClueId(id);
        return activities;
    }

    @Override
    public int insertActivityForClue(List<ClueActivityRelation> clueActivityRelations) {
        int flag = clueActivityRelationDao.insertActivityForClue(clueActivityRelations);
        return flag;
    }

    @Override
    public boolean removeActivityForClue(String id) {
        boolean flag = true;
        if (clueActivityRelationDao.removeActivityForClue(id) != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean clueConver(String clueId, Tran tran, String createBy) {

        //获取线索
        Clue clue ;
        clue = clueDao.selectOriginalClueByClueId(clueId);

        //线索中获取公司名称，进行查询是否存在客户
        String companyName = clue.getCompany();
        Customer customer = customerDao.selectOriginalCustomerByCompanyName(companyName);
        if (customer == null) {//不存在公司，即不存在客户，创建客户
            customer = new Customer();
            customer.setAddress(clue.getAddress());
            customer.setOwner(clue.getOwner());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setId(UUIDUtil.getUUID());
            customer.setName(clue.getCompany());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());

        }
        customer.setContactSummary(clue.getContactSummary());
        customer.setDescription(clue.getDescription());
        customer.setNextContactTime(clue.getNextContactTime());
        int flagCustomer = customerDao.insertCustomer(customer);


        //创建联系人信息
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        int flagContacts = contactsDao.insertContacts(contacts);

        //线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarks = clueRemarkDao.selectClueRemarkByClueId(clueId);
        List<ContactsRemark> contactsRemarks = new ArrayList<>();
        List<CustomerRemark> customerRemarks = new ArrayList<>();
        for (ClueRemark remark : clueRemarks) {

            ContactsRemark contactsRemark = new ContactsRemark();
            CustomerRemark customerRemark = new CustomerRemark();

            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setNoteContent(remark.getNoteContent());
            contactsRemark.setEditFlag("0");

            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setNoteContent(remark.getNoteContent());
            customerRemark.setEditFlag("0");

            contactsRemarks.add(contactsRemark);
            customerRemarks.add(customerRemark);

        }
        int contactsRemarkFlag = contactsRemarkDao.insertContactsRemarkBath(contactsRemarks);
        int customerRemarkFlag = customerRemarkDao.insertCustomerRemarkBath(customerRemarks);


        //“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.selectClueActivityRelation(clueId);
        List<ContactsActivityRelation> contactsActivityRelations = new ArrayList<>();
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelations.add(contactsActivityRelation);
        }
        int contactsActivityRelationFlag = contactsActivityRelationDao.insertContactsActivityRelationBath(contactsActivityRelations);

        //如果有创建交易需求，创建一条交易
        boolean isCreateTran = false;
        if (tran != null) {
            tran.setOwner(clue.getOwner());
            tran.setContactsId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setNextContactTime(clue.getNextContactTime());
            int tranFlag = tranDao.insertTran(tran);
            if (tranFlag == 1) {
                isCreateTran = true;
            }
        }

        //如果创建了交易，则创建一条该交易下的交易历史
        TranHistory tranHistory = new TranHistory();

        if (isCreateTran) {
            tranHistory.setCreateBy(createBy);
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());
            int tranHistoryFlag = tranHistoryDao.insertTranHistory(tranHistory);
        }
        //删除线索备注
        int deleteClueRemarkFlag = clueRemarkDao.deleteClueRemarkDao(clueId);
        //删除线索和市场活动的关系
        int deleteClueActivityRelation = clueActivityRelationDao.deleteRelation(clueId);
        //删除线索
        int deleteClue = clueDao.deleteClueById(clueId);

        return true;
    }
}
