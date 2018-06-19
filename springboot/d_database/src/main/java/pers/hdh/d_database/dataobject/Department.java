package pers.hdh.d_database.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Department class<br/>
 *
 * @author hdonghong
 * @date 2018/06/19
 */
@Data
@Entity
public class Department implements Serializable {

    private static final long serialVersionUID = -8263045269071570448L;

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    /**
     * 一对多，一方默认是LAZY
     * mappedBy这里以声明Many端的对象(User实体)的department属性提供了对应的映射关系
     * yml文件需要保持session的开启状态，spring.jpa.open-in-view=true
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Override
    public String toString() {
        return "[id = " + id + ", name = " + name + ", users = ]";
    }
}
