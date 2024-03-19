package tm.ugur.dto.auth;

public class AuthenticationClientDTO {

    private String phone;

    private String otp;

    private String platform;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "AuthenticationClientDTO{" +
                "phone='" + phone + '\'' +
                ", otp='" + otp + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
