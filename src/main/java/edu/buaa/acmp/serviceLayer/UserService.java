package edu.buaa.acmp.serviceLayer;

import com.github.pagehelper.PageHelper;
import edu.buaa.acmp.dataAccessLayer.domain.*;
import edu.buaa.acmp.dataAccessLayer.mapper.UserMapper;
import edu.buaa.acmp.util.PageDataWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 获得普通用户的信息
     */
    public User getUserInfoByID(String userID) throws RuntimeException{
        User user = userMapper.getUserInfo(userID);
        if(user == null)
            throw new RuntimeException("用户信息不存在");
        return user;
    }

    /**
     * 修改普通用户的信息，名字、头像、简介
     */
    public void updateUserInfo(User user){
        userMapper.updateUserInfo(user);
    }

    public void updatePassword(String userID, String password) {
        userMapper.updatePassword(userID, password);
    }

    /**
     * 用户收藏会议
     */
    public void collectConference(String userID,String conferID){
        userMapper.addCollect(userID, conferID);
    }

    /**
     * 判断当前会议是否被用户收藏
     * @return 收藏返回1，没收藏返回0
     */
    public Integer isCollectByUser(String userID,String conferID){
        return userMapper.isCollect(userID, conferID);
    }

    /**
     * 取消收藏会议
     */
    public void cancelCollect(String userID,String conferID){
        userMapper.cancelCollect(userID, conferID);
    }

    /**
     * 获得用户消息
     * 建议首先通过group by分开已读消息和未读消息，然后根据发送时间降序排列。
     */
    @NotNull
    public List<Message> getMessages(String userID, int index, int size, PageDataWrapper pageData,String type){
        try {
            if(type.equals("0")) {
                pageData.itemNum = userMapper.getMessagesCountNotRead(userID);
                pageData.pageNum = (int) Math.ceil(1.0 * pageData.itemNum / size);
                PageHelper.startPage(index, size);
                return userMapper.getMessagesNotRead(userID);
            }else {
                pageData.itemNum = userMapper.getMessagesCountRead(userID);
                pageData.pageNum = (int) Math.ceil(1.0 * pageData.itemNum / size);
                PageHelper.startPage(index, size);
                return userMapper.getMessagesRead(userID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    /**
     * 把消息标记为已读
     */
    public void readMessage(String msgID){
        userMapper.readMessage(msgID);
    }

    /**
     * 查看待审核的投稿
     */
    @NotNull
    public List<Contribution> getPendingContributions(String userID) {
        try {
            return userMapper.getPendingContributions(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看已通过的投稿
     */
    @NotNull
    public List<Contribution> getApprovedContributions(String userID) {
        try {
            return userMapper.getApprovedContributions(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看修改中的投稿
     */
    @NotNull
    public List<Contribution> getFixingContributions(String userID) {
        try {
            return userMapper.getFixingContributions(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看被拒绝的投稿
     */
    @NotNull
    public List<Contribution> getRejectedContributions(String userID) {
        try {
            return userMapper.getRejectedContributions(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获得投稿统计数据
     */
    @NotNull
    public ArrayList<Integer> getContributionTotal(String userID) {
        Integer pending = userMapper.getPendingContributionsCount(userID);
        Integer passed = userMapper.getApprovedContributionsCount(userID);
        Integer fixing = userMapper.getFixingContributionsCount(userID);
        Integer rejected = userMapper.getRejectedContributionsCount(userID);
        ArrayList<Integer> total = new ArrayList<>();
        total.add(pending);
        total.add(passed);
        total.add(fixing);
        total.add(rejected);
        return total;
    }

    /**
     * 查看已注册的未开幕的会议
     */
    @NotNull
    public List<Conference> getNotOpenConference(String userID) {
        try {
            return userMapper.getNotOpenConference(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看已注册的正在举行的会议
     */
    @NotNull
    public List<Conference> getOpeningConference(String userID) {
        try {
            return userMapper.getOpeningConference(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看已注册的已经结束的会议
     */
    @NotNull
    public List<Conference> getEndedConference(String userID) {
        try {
            return userMapper.getEndedConference(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看已注册的会议统计结果
     */
    @NotNull
    public ArrayList<Integer> getConferenceTotal(String userID){
        Integer notOpen = userMapper.getNotOpenConferenceCount(userID);
        Integer opening = userMapper.getOpeningConferenceCount(userID);
        Integer ended = userMapper.getEndedConferenceCount(userID);
        ArrayList<Integer> total = new ArrayList<>();
        total.add(notOpen);
        total.add(opening);
        total.add(ended);
        return total;
    }

    /**
     * 判断当前会议是否被用户注册
     * @return 已注册返回1，未注册返回0
     */
    public Integer isRegisterByUser(String userID, String conferID){
        return userMapper.isRegister(userID, conferID);
    }

    /**
     * 插入conference_register
     * 注意：需要返回给我那个自增长的ID
     */
    public BigInteger registerConference(ConferenceRegister register) {
        try {
            userMapper.registerConference(register);
            return register.id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigInteger.valueOf(0);
    }

    /**
     * 插入participant
     */
    public void insertParticipant(List<Participant> participants){
        try {
            for(Participant participant:participants) {
                userMapper.insertParticipant(participant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得某用户在某会议上被录取的论文信息
     * 如果返回为空数组，我就判断他是聆听者，否则就是作者
     */
    @NotNull
    public List<Contribution> getPassedContributionsByConferID(String conferID, String userID) {
        try {
            List<Contribution> contributionList = userMapper.getPassedContributionsByConferID(conferID, userID);
            if(contributionList.isEmpty())
                return Collections.emptyList();
            return contributionList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看收藏的未开幕的会议
     */
    @NotNull
    public List<Conference> getNotOpenConferenceCollected(String userID){
        try {
            return userMapper.getNotOpenConferenceCollected(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看收藏的正在举行的会议
     */
    @NotNull
    public List<Conference> getOpeningConferenceCollected(String userID) {
        try {
            return userMapper.getOpeningConferenceCollected(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看收藏的已经结束的会议
     */
    @NotNull
    public List<Conference> getEndedConferenceCollected(String userID){
        try {
            return userMapper.getEndedConferenceCollected(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 查看收藏的会议统计结果
     */
    @NotNull
    public ArrayList<Integer> getConferenceTotalCollected(String userID) {
        Integer notOpen = userMapper.getNotOpenConferenceCollectedCount(userID);
        Integer opening = userMapper.getOpeningConferenceCollectedCount(userID);
        Integer ended = userMapper.getEndedConferenceCollectedCount(userID);
        ArrayList<Integer> total = new ArrayList<>();
        total.add(notOpen);
        total.add(opening);
        total.add(ended);
        return total;
    }

}
