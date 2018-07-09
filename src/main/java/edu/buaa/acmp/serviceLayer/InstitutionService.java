package edu.buaa.acmp.serviceLayer;

import edu.buaa.acmp.dataAccessLayer.domain.Institution;
import edu.buaa.acmp.dataAccessLayer.domain.Principal;
import edu.buaa.acmp.dataAccessLayer.mapper.InstitutionMapper;
import edu.buaa.acmp.util.UuidUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionService {
    private final InstitutionMapper institutionMapper;

    public InstitutionService(InstitutionMapper institutionMapper){
        this.institutionMapper=institutionMapper;
    }

    /**
     * 得到需要审核的机构信息
     * @return
     * @throws Exception
     */
    public List<Institution> getInstitutionToCheck() throws Exception{
        return institutionMapper.getInstitutionToCheck();
    }

    /**
     * 设置机构审查状态
     * @param id
     * @param status
     * @throws Exception
     */
    public void setInstitutionStatus(String id,String status)throws Exception{
        try {
            if(Integer.parseInt(status)>1 || Integer.parseInt(status)<-1)throw new Exception("invalid");
            institutionMapper.setInstitutionStatus(id,status);

            if(status.equals("-1")){
//                String uuid = UuidUtils.generateUuid();
//                institutionMapper.setInstitutionName(uuid,id);
                //如果是-1，就删除记录
                institutionMapper.deleteInstitution(id);
            }
        }catch (Exception e){
            throw e;
        }

    }

    public Principal getPrincipal(String id){
        try{
            return institutionMapper.getPrincipal(id,"ALL");
        }catch (Exception e){
            return null;
        }
    }
}
