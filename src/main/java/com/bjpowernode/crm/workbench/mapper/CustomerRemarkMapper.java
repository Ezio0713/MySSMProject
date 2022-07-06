package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Fri Jun 17 19:39:34 CST 2022
     */
    int deleteByPrimaryKey(String id);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Fri Jun 17 19:39:34 CST 2022
     */
    int insertSelective(CustomerRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Fri Jun 17 19:39:34 CST 2022
     */
    CustomerRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Fri Jun 17 19:39:34 CST 2022
     */
    int updateByPrimaryKeySelective(CustomerRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Fri Jun 17 19:39:34 CST 2022
     */
    int updateByPrimaryKey(CustomerRemark row);
    /**
    创建客户备注
     */
    int insert(CustomerRemark row);
}