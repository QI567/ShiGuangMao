package com.yizhipin.data.response

/**
 * Creator Qi
 * Date 2018/12/4
 */
class ScheduleItemBean(
        val amount: String,//金额
        val createTime: String,//创建时间
        val dateMonth: String,//月份
        val id: String,
        val imgurl: String,
        val itemType: String,//子类型
        val nickname: String,
        val remark: String,
        val scheduleDate: String,//日期
        val status: String,//状态(0待完成,1已完成,2休息)
        val success: Boolean,//是否完成
        val taskId: String,// 任务ID(随行拍任务或者订单任务id)
        val teacherId: String,//老师ID
        val type: String,//类型(task或者order)
        val uid: String//服务用户id
) {
    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (other !is ScheduleItemBean) {
            return false
        }
        return other.scheduleDate == scheduleDate
    }
}