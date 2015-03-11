package models;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import play.db.jpa.JPA;

@Entity
@Table(name="user")
public class User  {
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	@Id
	private String id;

	@Column(length = 32, unique = true)
	// the optional @Column allows us makes sure that the name is limited to a
	// suitable size and is unique
	private String name;

	// note that no setter for ID is provided, Hibernate will generate the ID
	// for us
	
	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static List<User> getResultFromName(String name) {
		String sql="select hex(id),name from user";
		Query query =JPA.em().createNativeQuery(sql);
		@SuppressWarnings("unchecked")
		List<User> user = query.getResultList();
		return user;
		//return JPA.em().find(User.class, name);
	}

}
