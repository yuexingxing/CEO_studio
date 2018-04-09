package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2016/8/17.
 */
public class AppInfo {

    /**
     * lastVersion : 101
     * forcedVersion : 100
     * url : http://www.baidu.com
     * description : 新版本上线了！
     */

    private int lastVersion;
    private int forcedVersion;

    private String url;
    private String description;

    public void setLastVersion(int lastVersion) {
        this.lastVersion = lastVersion;
    }

    public void setForcedVersion(int forcedVersion) {
        this.forcedVersion = forcedVersion;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLastVersion() {
        return lastVersion;
    }

    public int getForcedVersion() {
        return forcedVersion;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

}
