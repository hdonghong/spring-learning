package pers.hdh.d_database.dataobject;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User class<br/>
 *
 * @author hdonghong
 * @date 2018/06/19
 */
@Data
@ToString
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 5608542878548547190L;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 姓名 */
    @Column
    private String name;

    /** 创建时间 */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /** 用户与部门 多对一，多方默认是EAGER */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

}
