package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    ContactsMapper contactsMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    ClueMapper clueMapper;
    @Autowired
    ClueRemarkMapper clueRemarkMapper;
    @Autowired
    ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    TranMapper tranMapper;
    @Autowired
    TranRemarkMapper tranRemarkMapper;
    @Autowired
    ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Override
    public int saveClue(Clue clue) {
        int insert = clueMapper.insert(clue);
        return insert;
    }
    //根据条件查询线索
    public List<Clue> queryClueByConditionOnPages(Map<String,Object> map) {
        List<Clue> clues = clueMapper.queryClueByConditionsOnpages(map);
        return clues;
    }
    //根据条件查询总页数
    public int queryClueCountByConditionOnPages(Map<String,Object> map){
        int i = clueMapper.queryClueCountByCondition(map);
        return i;
    }

    @Override
    public Clue queryClueById(String id) {
        Clue clue = clueMapper.selectByPrimaryKey(id);
        return clue;
    }

    //转换线索
    @Override
    public void saveConvertClue(Map<String, Object> map){
        Date date = new Date();
            Clue clue = clueMapper.selectByPrimaryKey((String)map.get("clueId"));
            User user = (User) map.get("user");
            Customer customer = new Customer();
            Contacts contacts = new Contacts();
            String uuid = UUIDUtils.getUUID();
            customer.setId(uuid);
            customer.setAddress(clue.getAddress());
            //添加客户
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(date));
            customer.setName(clue.getCompany());
            customer.setOwner(clue.getOwner());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());
            customerMapper.insert(customer);
            //添加联系人
            contacts.setAddress(clue.getAddress());
            contacts.setAppellation(clue.getAppellation());
            contacts.setCreateBy(user.getId());
            contacts.setCreateTime(DateUtils.formateDateTime(date));
            contacts.setContactSummary(clue.getContactSummary());
            contacts.setId(UUIDUtils.getUUID());
            contacts.setCustomerId(uuid);
            contacts.setEmail(clue.getEmail());
            contacts.setFullname(clue.getFullname());
            contacts.setJob(clue.getJob());
            contacts.setSource(clue.getSource());
            contacts.setMphone(clue.getMphone());
            contacts.setNextContactTime(clue.getNextContactTime());
            contacts.setOwner(clue.getOwner());
            contactsMapper.insert(contacts);
        List<ClueRemark> clueRemarks = clueRemarkMapper.queryClueRemarkByClueId(clue.getId());
        for (ClueRemark clueRemark:clueRemarks) {
            //给联系人添加备注
            ContactsRemark contactsRemark = new ContactsRemark();
            CustomerRemark customerRemark = new CustomerRemark();
            contactsRemark.setId(UUIDUtils.getUUID());
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateBy(clueRemark.getCreateBy());
            contactsRemark.setCreateTime(clueRemark.getCreateTime());
            contactsRemark.setEditBy(clueRemark.getEditBy());
            contactsRemark.setEditFlag(clueRemark.getEditFlag());
            contactsRemark.setEditTime(clueRemark.getEditTime());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemarkMapper.insert(contactsRemark);
            //给客户添加备注
            customerRemark.setId(UUIDUtils.getUUID());
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateBy(clueRemark.getCreateBy());
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            customerRemark.setEditBy(clueRemark.getEditBy());
            customerRemark.setEditFlag(clueRemark.getEditFlag());
            customerRemark.setEditTime(clueRemark.getEditTime());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemarkMapper.insert(customerRemark);
        }
        String isCreateTran = (String)map.get("isCreateTran");
        if (isCreateTran.equals("true")){
            Tran tran = new Tran();
            String money = (String)map.get("money");
            String name = (String)map.get("name");
            String expectedDate = (String)map.get("expectedDate");
            String stage = (String)map.get("stage");
            //添加交易
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate(expectedDate);
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney(money);
            tran.setName(name);
            tran.setOwner(user.getId());
            tran.setStage(stage);
            tranMapper.saveCreateTran(tran);
            //转换线索与市场活动关联为联系人市场活动关联
            String[] strings = clueActivityRelationMapper.queryRelationByClueId(clue.getId());
            List<ContactsActivityRelation> list = new ArrayList<>();
                     if (strings!=null && strings.length!=0 ){
                         for (String activityId:strings){
                            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                            contactsActivityRelation.setId(UUIDUtils.getUUID());
                            contactsActivityRelation.setContactsId(contacts.getId());
                            contactsActivityRelation.setActivityId(activityId);
                            list.add(contactsActivityRelation);
                         }
                         contactsActivityRelationMapper.insert(list);
                     }
            if (clueRemarks != null && clueRemarks.size()>0){
                List<TranRemark> remarkList = new ArrayList<>();
                for (ClueRemark clueRemark:clueRemarks) {
                    TranRemark tranRemark = new TranRemark();
                    tranRemark.setCreateBy(clueRemark.getCreateBy());
                    tranRemark.setCreateTime(clueRemark.getCreateTime());
                    tranRemark.setEditBy(clueRemark.getEditBy());
                    tranRemark.setEditFlag(clueRemark.getEditFlag());
                    tranRemark.setId(UUIDUtils.getUUID());
                    tranRemark.setEditTime(clueRemark.getEditTime());
                    tranRemark.setTranId(tran.getId());
                    tranRemark.setNoteContent(clueRemark.getNoteContent());
                    remarkList.add(tranRemark);
                }
                tranRemarkMapper.insertList(remarkList);
            }
        }
        //删除该条线索，及相关评论和市场活动关系
        clueActivityRelationMapper.deleteByClueId(clue.getId());
        clueRemarkMapper.deletClueRemarkByClueId(clue.getId());
        clueMapper.deleteByPrimaryKey(clue.getId());
    }
}
