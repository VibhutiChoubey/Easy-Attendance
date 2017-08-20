package model_class;

public class Attendance_database {

    public String class_name;
    public int roll;
    public String student_name;
    public String device_id;

    public String getDevice_id() {return device_id;}

    public void setDevice_id(String device_id) {this.device_id = device_id;}

    public int getRoll() {return roll;}

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
