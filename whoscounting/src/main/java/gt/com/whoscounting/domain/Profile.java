package gt.com.whoscounting.domain;

import gt.com.whoscounting.form.ProfileForm.Country;
import gt.com.whoscounting.form.ProfileForm.Gender;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


// TODO indicate that this class is an Entity
@Entity
public class Profile {
	String displayName;
	String mainEmail;
	
	private Country country;
    
    private Gender gender;
	
	private List<String> miIDoAnswers = new ArrayList<String>();
	private List<String> miIDontAnswers = new ArrayList<String>();

    
	public List<String> getMiIDoAnswers(){
		return ImmutableList.copyOf(miIDoAnswers);
	}
	
	public List<String> getMiIDontAnswers(){
		return ImmutableList.copyOf(miIDontAnswers);
	}
	
	public int answerQuestion(String questionKey, boolean answer) {
		int valorIDo = 0;
		if(answer){
			if(!miIDoAnswers.contains(questionKey)){
				if(miIDontAnswers.contains(questionKey)){
					miIDontAnswers.remove(questionKey);
				}
				miIDoAnswers.add(questionKey);
				valorIDo = 1;
			}
		}
		else {
			if(!miIDontAnswers.contains(questionKey)){
				if(miIDoAnswers.contains(questionKey)){
					miIDoAnswers.remove(questionKey);
					valorIDo = -1;
				}
				miIDontAnswers.add(questionKey);
			}
		}
		return valorIDo;
	}
	
	
	
	// TODO indicate that the userId is to be used in the Entity's key
	@Id String userId;
    
    /**
     * Public constructor for Profile.
     * @param userId The user id, obtained from the email
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * @param teeShirtSize The User's tee shirt size
     * 
     */
    public Profile (String userId, String displayName, String mainEmail, Country country, Gender gender) {
    	this.userId = userId;
    	this.displayName = displayName;
    	this.mainEmail = mainEmail;
    	this.country = country;
    	this.gender = gender;
    }
    
	public String getDisplayName() {
		return displayName;
	}

	public String getMainEmail() {
		return mainEmail;
	}

	public Country getCountry() {
		return country;
	}
	
	public Gender getGender() {
		return gender;
	}

	public String getUserId() {
		return userId;
	}

	public void updateProfile(String displayName, Country country, Gender gender) {
		if(displayName != null){
			this.displayName = displayName;
		}
		if(country != null){
			this.country = country;
		}
		if(gender != null){
			this.gender = gender;
		}
	}
	
	
	
	/**
     * Just making the default constructor private.
     */
    private Profile() {}

}
