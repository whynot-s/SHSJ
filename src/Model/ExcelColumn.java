package Model;

public enum ExcelColumn {

/*
    Column 1: 序号（行号）
    Column 2: 提交时间
    Column 3: 无用 所用时间
    Column 4: 无用 来源
    Column 5: 无用 来源详情
    Column 6: 无用 来自IP
    Column 7: 立项单位编号 - 系号
    Column 8: 实践方向
        - 1 经济发展
        - 2 科学创新
        - 3 教育发展
        - 4 社会现象
        - 5 民俗文化
        - 6 返乡宣传
        - 7 其他
    Column 9: 实践队名
    Column 10: 队长姓名
    Column 11: 队长学号
    Column 12: 队长电话
    Column 13: 专业指导教师姓名
    Column 14: 专业指导教师电话
    Column 15: 思政指导教师姓名
    Column 16: 思政指导教师电话
    寒假项目（5人）Column 17-31: 依次为队员姓名、学号、电话
    寒假项目（国内限制5个地点，国（境）外限制3个地点）
    Column 32: 包含
    Column 33: 是否出国（境）
    Column 34-48: 国内5个开始时间、结束时间、地点
    Column 49-60: 国外3个开始时间、结束时间、地点
    暑假项目（15人）Column 17-62: 依次为队员姓名、学号、电话
 */

    LINE(1, 1),
    SUBMIT(2, 2),
    DEP(7, 7),
    DIRECTION(8, 8),
    TEAM_NAME(9, 9),
    LEADER_NAME(10, 10),
    LEADER_ID(11, 11),
    LEADER_PHONE(12, 12),
    TEACHER1_NAME(13, 13),
    TEACHER1_PHONE(14, 14),
    TEACHER2_NAME(15, 15),
    TEACHER2_PHONE(16, 16),
    STU_START(17, 17),
    STU_END(62, 31);


    private int sIndex;
    private int wIndex;

    ExcelColumn(int summer_index, int winter_index){
        sIndex = summer_index;
        wIndex = winter_index;
    }

    public int Index(boolean isWinter) {
        if(isWinter) return wIndex - 1;
        else return sIndex - 1;
    }


}
