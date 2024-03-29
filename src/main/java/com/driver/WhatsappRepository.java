package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) {
        if(userMobile.contains(mobile)){
            try {
                throw new Exception();
            } catch (Exception e) {
               return "User already exists";
            }
        }
          User user= new User(name,mobile);
            userMobile.add(mobile);
            return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group= new Group();
        if(users.size()==2){
            group.setName(users.get(1).getName());
            group.setNumberOfParticipants(2);
            return group;
        }
        customGroupCount++;
        group.setName("Group "+customGroupCount);
        group.setNumberOfParticipants(users.size());
        adminMap.put(group,users.get(0));
        groupUserMap.put(group,users);
        return group;

    }

    public int createMessage(String content) {
        messageId++;
        Message message= new Message(messageId,content,new Date());
        return message.getId();
    }

    public int sendMessage(Message message, User sender, Group group) {
        List<Message> listOfMessages= new ArrayList<>();
        if(!groupUserMap.containsKey(group)){
            try {
                throw new Exception();
            } catch (Exception e) {
                System.out.println("Group does not exist");
            }
        }else if(!groupUserMap.get(group).contains(sender)){
            try {
                throw new Exception();
            } catch (Exception e) {
                System.out.println("You are not allowed to send message");
            }
        }
        listOfMessages.add(message);
        groupMessageMap.put(group,listOfMessages);
        return listOfMessages.size();
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!groupUserMap.containsKey(group)){
            try {
                throw new Exception();
            } catch (Exception e) {
                return "Group does not exist";
            }
        }else if(adminMap.get(group)!=approver) {
            try {
                throw new Exception();
            } catch (Exception e) {
                return "Approver does not have rights";
            }
        }
        else if(!groupUserMap.get(group).contains(user)){
            try {
                throw new Exception();
            } catch (Exception e) {
                return "User is not a participant";
            }
        }
         adminMap.put(group,user);
        return "SUCCESS";
        }



    public int removeUser(User user) {
        return 0;
    }

    public String findMessage(Date start, Date end, int k) {
        return "success";
    }
}
