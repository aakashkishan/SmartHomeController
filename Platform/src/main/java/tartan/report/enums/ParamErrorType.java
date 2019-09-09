package tartan.report.enums;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum ParamErrorType{

    NONE(0), EMPTY(1), INVALID_DATE(2);

    private static final SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    private int value;

    private ParamErrorType(int value){
        this.value = value;
    }

    public static @NonNull ParamErrorType errorType(int value){
        return NONE;
    }

    public static @NonNull ParamErrorType errorType(@Nullable Boolean value){
        return value == null ? EMPTY: NONE;
    }

    public static ParamErrorType errorType(boolean value){
        return NONE;
    }

    public static @NonNull ParamErrorType errorType(@Nullable String value){
        return (value == null || value.isEmpty()) ? EMPTY : NONE;
    }

    public static ParamErrorType isValidDate(@Nullable String value){
        if (value == null || value.isEmpty()){
            return EMPTY;
        }
        try {
            Date date = formatter.parse(value);
            return NONE;
        } catch (ParseException e) {
            return INVALID_DATE;
        }
    }
}
