package com.schooldevops.springboot.mongodbsamples

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task
import com.schooldevops.springboot.mongodbsamples.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class ProjectController(
    @Autowired val projectService: ProjectService
) {
    @PostMapping("/projects")
    fun saveProject(@RequestBody p: Project) : String{
        projectService.saveProject(p)
        return "OK"
    }

    @PostMapping("/tasks")
    fun saveTask(@RequestBody t: Task): String {
        projectService.saveTask(t)
        return "OK"
    }

    @GetMapping("/projects")
    fun findProject(@RequestParam id: String): ResponseEntity<Project> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findProject(id))
    }

    @GetMapping("/tasks")
    fun findTask(@RequestParam id: String): ResponseEntity<Task> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findTask(id))
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<String> {
        projectService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).body(id)
    }

    @DeleteMapping("/projects/{id}")
    fun deleteProject(@PathVariable id: String): ResponseEntity<String> {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body(id)
    }

    @PutMapping("/projects")
    fun updateProject(@RequestBody p: Project) : Unit {
        projectService.updateProject(p)
    }

    @GetMapping("/api/template/sqlBySndId")
    fun sqlBySndId(): ResponseEntity<PersonalInfoQuery> {
        val personalInfoQuery = PersonalInfoQuery()
        personalInfoQuery.psnlItmSqlPhrs = newQuery()
        return ResponseEntity.ok(personalInfoQuery)
    }

    fun newQuery(): String {
        return """&lt;![CDATA[
with prd_cnt 
as (select count(*) prd_cnt 
         , (select sum(ppp.SCB_PRC)
              from ord.ord_odr_prd oop2
                 , prd.prd_prd_prc ppp 
             where oop2.ODR_NUM = oop.odr_num 
               and ppp.prd_id = oop2.PRD_ID) scb_prc
      from ord.ord_odr_prd oop 
     where oop.ODR_NUM = '2023108895066') ,
add_amt
as (select sum(inv1.ADD_STRD_AMT) ADD_STRD_AMT
         , sum(inv1.EXL_DDCT_AMT) EXL_DDCT_AMT
         , sum(inv1.add_add_bill_amt) add_add_bill_amt
         , sum(inv1.bas_add_bill_amt) bas_add_bill_amt
      from (select (select case when pps.BAS_BNF_PRD_CNT  &lt; (select count(*)
                                                               from ord.ord_odr_comp_prd oocp
                                                              where oocp.ODR_NUM = oop3.ODR_NUM
                                                                and oocp.SLOT_TYP_CD = '02') then pps.ADD_STRD_AMT 
                                else 0 
                           end 
                      from prd.prd_prd_slot pps 
                     where pps.PRD_ID = oop3.PRD_ID 
                       and pps.SLOT_TYP_CD = '02') add_strd_amt
                 , (select pscp.EXL_DDCT_AMT 
                      from prd.prd_slot_comp_prd pscp 
                     where pscp.PRD_ID = oop3.PRD_ID 
                       and pscp.SLOT_TYP_CD = '01'
                       and pscp.EXL_DDCT_AMT &gt; 0
                       and not exists (select 1
                                         from ord.ord_odr_comp_prd oocp2
                                        where oocp2.ODR_NUM = oop3.ODR_NUM
                                          and oocp2.PRD_ID = pscp.PRD_ID 
                                          and oocp2.SLOT_TYP_CD = '01'
                                          and oocp2.COMP_PRD_ID  = pscp.COMP_PRD_ID)) EXL_DDCT_AMT
                 , (select pscp.ADD_BILL_AMT 
                      from prd.prd_slot_comp_prd pscp 
                     where pscp.PRD_ID = oop3.PRD_ID 
                       and pscp.SLOT_TYP_CD = '02'
                       and pscp.ADD_BILL_AMT  &gt; 0
                       and exists (select 1
                                     from ord.ord_odr_comp_prd oocp2
                                    where oocp2.ODR_NUM = oop3.ODR_NUM
                                      and oocp2.PRD_ID = pscp.PRD_ID 
                                      and oocp2.SLOT_TYP_CD = '02'
                                      and oocp2.COMP_PRD_ID  = pscp.COMP_PRD_ID)) add_add_bill_amt
                 , (select pscp.ADD_BILL_AMT 
                      from prd.prd_slot_comp_prd pscp 
                     where pscp.PRD_ID = oop3.PRD_ID 
                       and pscp.SLOT_TYP_CD = '01'
                       and pscp.ADD_BILL_AMT  &gt; 0
                       and exists (select 1
                                     from ord.ord_odr_comp_prd oocp2
                                    where oocp2.ODR_NUM = oop3.ODR_NUM
                                      and oocp2.PRD_ID = pscp.PRD_ID 
                                      and oocp2.SLOT_TYP_CD = '01'
                                      and oocp2.COMP_PRD_ID  = pscp.COMP_PRD_ID)) bas_add_bill_amt
              from ord.ord_odr_prd oop3 
             where oop3.ODR_NUM = '2023108895066'
             ) inv1
      ),
rep_prd 
as (select pp.prd_nm
         , pp.SCB_PAY_TRM_CD 
         , (select cccd.COM_CD_VAL_NM  
              from com.com_com_cd_dtl cccd 
             where cccd.COM_CD_ID = 'PRD_C_P0018' 
               and cccd.COM_CD_VAL = pp.SCB_PAY_TRM_CD 
               and cccd.USE_YN = 'Y') SCB_PAY_TRM_NM
         , (select concat(ppic.PRD_IMG_FILE_PATH_NM ,ppic.PRD_IMG_ORGL_FILE_NM )
              from prd.prd_prd_img_cntn ppic
             where ppic.PRD_ID = pp .prd_id
               and ppic.IMG_KND_CD = '01'
             order by IMG_CNTN_SEQ 
             limit 1) PRD_IMG_FILE_CDN_URL
         , pp.RCS_MMS_PHRS 
      from ord.ord_odr_prd oop 
         , prd.prd_prd pp 
     where oop.ODR_NUM = '2023108895066'
       and pp.prd_id = oop.prd_id
     order by pp.PRD_TYP_CD , oop.CTR_NUM  desc
     limit 1
) ,
settl_info
as (select ppr.PAY_AMT 
         , ppr.PAY_PT_AMT 
         , ppr.PAY_CPN_AMT 
         , ppr.PAY_VOU_AMT 
         , ppr.PAY_CAL_AMT 
         , (select sum(pfcs.DC_AMT)
              from pay.pay_fee_cal_spc pfcs
             where pfcs.FEE_CAL_SPC_NUM = ppr.FEE_CAL_SPC_NUM ) dc_amt
      from pay.pay_pay_req ppr 
     where ppr.PAY_NUM = NULL
) 
select mm.MBR_NM custNm  -- 회원명
     , case when prd_cnt &gt; 1 then concat(rp.prd_nm,'/',rp.SCB_PAY_TRM_NM, ' 외 주문 상품수 ',pc.prd_cnt - 1,'개' )
            else concat(rp.prd_nm,'/',rp.SCB_PAY_TRM_NM)
       end refpPrdNm  -- {대표상품명}/ {대표상품 구독주기} {외 {주문 상품수-1}개}
     , format(scb_prc + ifnull(aa.ADD_STRD_AMT,0) - ifnull(aa.EXL_DDCT_AMT,0) + ifnull(aa.add_add_bill_amt,0) + ifnull(aa.bas_add_bill_amt,0),0) totalSalePrice  -- 판매가 합계
     , format(ifnull(si.PAY_CAL_AMT,0),0) totalPayAmt  -- 결제금액 합계 (실제 금융사에 결제하는 금액만 대상)
     , case when (ifnull(si.dc_amt,0) + ifnull(si.pay_cpn_amt,0) + ifnull(si.PAY_PT_AMT,0) + ifnull(si.pay_vou_amt,0)) &gt; 0 
                 then concat('(-',format(ifnull(si.dc_amt,0) + ifnull(si.PAY_PT_AMT,0) + ifnull(si.pay_cpn_amt,0) + ifnull(si.pay_vou_amt,0),0) , '원 할인)')
            else ''
       end totalDiscountAmt
     , rp.PRD_IMG_FILE_CDN_URL refpPrdRepImgURL
  from ord.ord_odr oo 
     , mem.mem_mbr mm 
     , prd_cnt pc
     , rep_prd rp
     , add_amt aa
     , settl_info si
 where oo.odr_num = '2023108895066'
   and mm.MBR_NUM = oo.ODR_MBR_NUM
]]&gt;"""
    }
}