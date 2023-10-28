package com.lineying.common;

/**
 * *********************************************
 *
 * @ClassName: ComponentEnum
 * @Description 成分枚举
 * @Author ganjing
 * @Date 2019年4月27日
 * @Copyright: 2019 天誉飞歌(重庆)研究院版权所有
 * *********************************************
 */
public enum ComponentEnum {
    CALORY("卡路里", "calory"),
    FAT("脂肪", "fat"),
    PROTEIN("蛋白质", "protein"),
    FIBER_DIETARY("膳食纤维", "fiber_dietary"),
    CARBOHYDRATE("碳水化合物", "carbohydrate"),
    CAROTENE("胡萝卜素", "carotene"),
    VITAMIN_A("维生素A", "vitamin_a"),
    VITAMIN_B1("维生素B1", "vitamin_b1"),
    VITAMIN_B2("维生素B2", "vitamin_b2"),
    THIAMINE("维生素B1", "thiamine"),
    LACTOFLAVIN("维生素B2", "lactoflavin"),
    VITAMIN_C("维生素C", "vitamin_c"),
    VITAMIN_E("维生素E", "vitamin_e"),
    NIACIN("烟酸", "niacin"),
    NATRIUM("钠", "natrium"),
    CALCIUM("钙", "calcium"),
    IRON("铁", "iron"),
    KALIUM("钾", "kalium"),
    IODINE("碘", "iodine"),
    ZINC("锌", "zinc"),
    SELENIUM("硒", "selenium"),
    MAGNESIUM("镁", "magnesium"),
    COPPER("铜", "copper"),
    MANGANESE("锰", "manganese"),
    CHOLESTEROL("胆固醇", "cholesterol");
    /**
     * 状态吗
     */
    private String name;
    private String sqlName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    private ComponentEnum(String name, String sqlName) {
        this.name = name;
        this.sqlName = sqlName;
    }

    /**
     * 根据code获取去value
     *
     * @param code
     * @return
     */
    public static String getValueByMsg(String code) {
        for (ComponentEnum buyType : ComponentEnum.values()) {
            if (code.equals(buyType.getName())) {
                return buyType.getSqlName();
            }
        }
        return null;
    }
}
