package edu.buaa.acmp.serviceLayer;

import com.github.pagehelper.PageHelper;
import edu.buaa.acmp.dataAccessLayer.domain.*;
import edu.buaa.acmp.dataAccessLayer.mapper.PrincipalMapper;
import edu.buaa.acmp.util.PageDataWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class PrincipalService {
    private final PrincipalMapper principalMapper;

    public PrincipalService(PrincipalMapper principalMapper) {
        this.principalMapper = principalMapper;
    }

    /**
     * 获得主办方负责人的信息，
     */
    public Principal getPrincipalInfo(String prinID) throws RuntimeException {
        Principal principal = principalMapper.getPrincipalInfo(prinID);
        if(principal == null)
            throw new RuntimeException("工作人员信息不存在");
        return principal;
    }

    /**
     * 获得主办方机构信息
     */
    public Institution getInsInfo(String insID) throws RuntimeException{
        Institution institution = principalMapper.getInstitutionInfo(insID);
        if(institution == null)
            throw new RuntimeException("主办方信息不存在");
        return institution;
    }

    /**
     * 更新主办方负责人信息
     * 注意：需要更新的字段见api
     */
    public void updatePrincpalInfo(Principal principal){
        principalMapper.updatePrincipal(principal);
    }

    /**
     * 更新机构信息
     * 注意：需要更新的字段见api
     */
    public void updateInsInfo(Institution ins){
        principalMapper.updateInstitution(ins);
    }

    /**
     * 修改密码
     */
    public void updatePassword(String userID, String password) {
        principalMapper.updatePassword(userID, password);
    }

    /**
     * 添加负责人
     */
    public void addPrincipal(Principal principal){
        principal.power = "GENERAL";
        principalMapper.addPrincipal(principal);
    }

    /**
     * 获得某机构的所有负责人
     */
    @NotNull
    public List<Principal> getAllPrincipal(String insID) {
        try {
            return principalMapper.getAllPrincipal(insID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获得某会议下的注册人员
     */
    @NotNull
    public List<ConferenceRegister> getAllRegisters(String conferID, int index, int size, PageDataWrapper pageData) {
        try {
            pageData.pageNum = (int) Math.ceil(1.0 * principalMapper.getAllRegistersCount(conferID) / size);
            PageHelper.startPage(index, size);
            return principalMapper.getAllRegisters(conferID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获得注册的参与者 participant
     */
    @NotNull
    public List<Participant> getAllParticipants(String registerID) {
        try {
            return principalMapper.getAllParticipants(registerID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 更新某条评审记录
     * @param action  1 approve, 2 need to fix, 3 reject
     * @param suggestion 修改建议
     */
    @Transactional
    public void updateReview(String reviewID, String suggestion, String action){
        principalMapper.updateReview(action, suggestion, reviewID);
        Integer id = principalMapper.getContributionIDByReviewID(reviewID);
        principalMapper.updateContributionState(action,id.toString());
    }

}
