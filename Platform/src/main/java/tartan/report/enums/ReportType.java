package tartan.report.enums;

public enum ReportType {

    NONE(0), LIGHT(1), HVAC(2), LIGHT_HVAC(3), ENERGY(4), ENERGY_LIGHT(5), ENERGY_HVAC(6), ALL(7);

    private int value;
    private ReportType(int value){
        this.value = value;
    }


    public static ReportType getType(boolean requireLight, boolean requireHVAC, boolean requireEnergy){
        int light = (requireLight ? 1: 0);
        int HVAC = (requireHVAC ? 1 : 0) << 1;
        int energy = (requireEnergy ? 1: 0) << 2;

        int value = energy | HVAC | light;

        return ReportType.values()[value];
    }

    public static boolean isShowLight(ReportType type){
        return (type.value & LIGHT.value) == LIGHT.value;
    }

    public static boolean isShowHVAC(ReportType type){
        return (type.value & HVAC.value) == HVAC.value;
    }

    public static boolean isShowEnergy(ReportType type){
        return (type.value & ENERGY.value) == ENERGY.value;
    }
}
