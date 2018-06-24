package pers.hdh.f_test.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hdonghong
 */
@Data
@Entity
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "name")
	private String name;
}
