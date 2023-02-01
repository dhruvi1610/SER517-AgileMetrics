package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackIcon {
    private String image_34;
    private String image_44;
    private String image_68;
    private String image_88;
    private String image_102;
    private String image_132;
    private String image_230;
    private boolean image_default;

    public SlackIcon() {

    }

    /**
     * @return the image_34
     */
    public String getImage_34() {
        return image_34;
    }

    /**
     * @param image_34 the image_34 to set
     */
    public void setImage_34(String image_34) {
        this.image_34 = image_34;
    }

    /**
     * @return the image_44
     */
    public String getImage_44() {
        return image_44;
    }

    /**
     * @param image_44 the image_44 to set
     */
    public void setImage_44(String image_44) {
        this.image_44 = image_44;
    }

    /**
     * @return the image_68
     */
    public String getImage_68() {
        return image_68;
    }

    /**
     * @param image_68 the image_68 to set
     */
    public void setImage_68(String image_68) {
        this.image_68 = image_68;
    }

    /**
     * @return the image_88
     */
    public String getImage_88() {
        return image_88;
    }

    /**
     * @param image_88 the image_88 to set
     */
    public void setImage_88(String image_88) {
        this.image_88 = image_88;
    }

    /**
     * @return the image_102
     */
    public String getImage_102() {
        return image_102;
    }

    /**
     * @param image_102 the image_102 to set
     */
    public void setImage_102(String image_102) {
        this.image_102 = image_102;
    }

    /**
     * @return the image_132
     */
    public String getImage_132() {
        return image_132;
    }

    /**
     * @param image_132 the image_132 to set
     */
    public void setImage_132(String image_132) {
        this.image_132 = image_132;
    }

    /**
     * @return the image_230
     */
    public String getImage_230() {
        return image_230;
    }

    /**
     * @param image_230 the image_230 to set
     */
    public void setImage_230(String image_230) {
        this.image_230 = image_230;
    }

    /**
     * @return the image_default
     */
    public boolean isImage_default() {
        return image_default;
    }

    /**
     * @param image_default the image_default to set
     */
    public void setImage_default(boolean image_default) {
        this.image_default = image_default;
    }

}
