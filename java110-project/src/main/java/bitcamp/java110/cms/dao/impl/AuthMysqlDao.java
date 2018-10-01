package bitcamp.java110.cms.dao.impl;

import bitcamp.java110.cms.dao.AuthDao;
import bitcamp.java110.cms.util.DataSource;

public class AuthMysqlDao implements AuthDao {

    DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}









