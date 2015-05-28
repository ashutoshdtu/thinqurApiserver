/**
 * this file specifies the class structure of Question
 */
package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import org.joda.time.*;

/** class to store Question
 * 
 * @author ashutosh
 * 
 */
@Entity
/*@Indexes({
	   @Index("user, -cs"),
	   @Index("changedRecord, -cs")})
	   */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
	
	public enum Type {
		MCSC,        // Multiple choice single correct 
		MCMC         // Multiple choice multiple correct
	}
	
	@JsonIgnore
	@Id
	ObjectId _id = new ObjectId();
	
	String id = _id.toString();
	
	public String lastUpdatedAt = new DateTime( DateTimeZone.UTC ).toString();
	public String createdAt = new DateTime( DateTimeZone.UTC ).toString();
	public boolean isAnonymous = false;
	public Type type = Type.MCSC;
	
	@Required
	@MaxLength(150)
	public String statement;
	
	public String imageURL;
	
	//@MaxLength(700)
	//public String description;
	
	@Embedded
	public List<Answer> answers = new ArrayList<Answer>();
	
	@Embedded
	public List<Tag> tags = new ArrayList<Tag>();
	
	@Required
	@Embedded
	public UserRef createdBy = null;
	
	@Embedded
	public List<UserRef> updatedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> followedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> upvotedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> downvotedBy = new ArrayList<UserRef>();
	
	@NotSaved
	int totalUpdates; 
	
	@NotSaved
	int totalFollowers;
	
	@NotSaved
	int totalUpvotes;
	
	@NotSaved
	int totalDownvotes;
	
	
	
	
	/*
	 * User specific information
	 */
	@NotSaved
	public String userId = null;

	@NotSaved
	public boolean isUpdatedByUser = false;
	
	@NotSaved
	public boolean isFollowedByUser = false;
	
	@NotSaved
	public boolean isQuestionUpvotedByUser = false;

	@NotSaved
	public boolean isQuestionDownvotedByUser = false;

	@NotSaved
	public boolean isAnswerUpvotedByUser = false;

	@NotSaved
	public String answerUpvotedByUser = null;
	
	
	
	
	/*
	 * Temp variables
	 */
	@NotSaved
	@JsonIgnore
	Map<String, Integer> updatedByMap = new HashMap<String, Integer>();
	
	@NotSaved
	@JsonIgnore
	Map<String, Integer> followedByMap = new HashMap<String, Integer>();
	
	@NotSaved
	@JsonIgnore
	Map<String, Integer> upvotedByMap = new HashMap<String, Integer>();
	
	@NotSaved
	@JsonIgnore
	Map<String, Integer> downvotedByMap = new HashMap<String, Integer>();
	
	@NotSaved
	@JsonIgnore
	Map<String, Map<String, Integer>> answeredByMap = new HashMap<String, Map<String,Integer>>();
	
	
	
	
	@PrePersist void prePersist() {lastUpdatedAt = new DateTime( DateTimeZone.UTC ).toString();}
	
	public void set_id(ObjectId _id) {
		this._id = _id;
		this.id = _id.toString();
	}
	
	public void setId(String id) {
		this.id = id;
		this._id = new ObjectId(id);
	}
	
	public ObjectId get_id() {
		return _id;
	}
	
	public String getId() {
		return id;
	}
	
	public int getTotalUpdates() {
		return updatedBy != null ? updatedBy.size() : 0;
	}
	
	public void setTotalUpdates(int totalUpdates) {}
	
	public int getTotalFollowers() {
		return followedBy != null ? followedBy.size() : 0;
	}
	
	public void setTotalFollowers(int totalFollowers) {}
	
	public int getTotalUpvotes() {
		return upvotedBy != null ? upvotedBy.size() : 0;
	}
	
	public void setTotalUpvotes(int totalUpvotes) {}
	
	public int getTotalDownvotes() {
		return downvotedBy != null ? downvotedBy.size() : 0;
	}
	
	public void setTotalDownvotes(int totalDownvotes) {}
	
	public void setUserId(String userId) {
		this.userId = userId;
		calculateUpdatedByMap();
		calculateFollowedByMap();
		calculateUpvotedByMap();
		calculateDownvotedByMap();
		calculateAnsweredByMap();
		if(updatedByMap.containsKey(userId)) {
			isUpdatedByUser = true;
		}
		if(followedByMap.containsKey(userId)) {
			isFollowedByUser = true;
		}
		if(upvotedByMap.containsKey(userId)) {
			isQuestionUpvotedByUser = true;
		}
		if(downvotedByMap.containsKey(userId)) {
			isQuestionDownvotedByUser = true;
		}
		for(String answerId: answeredByMap.keySet()) {
			if(answeredByMap.get(answerId).containsKey(userId)){
				isAnswerUpvotedByUser = true;
				answerUpvotedByUser = answerId;
			}
		}
	}

	private void calculateUpdatedByMap() {
		if(updatedBy!=null && updatedBy.size()>0) {
			for(UserRef user : updatedBy) {
				updatedByMap.put(user.id, 1);
			}
		}
	}
	
	private void calculateFollowedByMap() {
		if(followedBy!=null && followedBy.size()>0) {
			for(UserRef user : followedBy) {
				followedByMap.put(user.id, 1);
			}
		}
	}
	
	private void calculateUpvotedByMap() {
		if(upvotedBy!=null && upvotedBy.size()>0) {
			for(UserRef user : upvotedBy) {
				upvotedByMap.put(user.id, 1);
			}
		}
	}
	
	private void calculateDownvotedByMap() {
		if(downvotedBy!=null && downvotedBy.size()>0) {
			for(UserRef user : downvotedBy) {
				downvotedByMap.put(user.id, 1);
			}
		}
	}
	
	private void calculateAnsweredByMap() {
		if(answers!=null && answers.size()>0) {
			for(Answer answer: answers) {
				Map<String, Integer> upvotedByMap = new HashMap<String, Integer>();
				if(answer.upvotedBy!=null && answer.upvotedBy.size()>0) {
					for(UserRef user : answer.upvotedBy) {
						upvotedByMap.put(user.id, 1);
					}
				}
				answeredByMap.put(answer.id, upvotedByMap);
			}
		}
	}
}
