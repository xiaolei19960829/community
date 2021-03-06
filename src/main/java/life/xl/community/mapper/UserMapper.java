package life.xl.community.mapper;

import life.xl.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Author: XiaoLei
 * @Date Created in 16:33 2020/3/16
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
     void insert(User user);
}
