package com.karlnosworthy.bitbucketclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by karlnosworthy on 10/08/15.
 */
public class BitBucketRepository {

    @Expose
    private String scm;

    @Expose
    private String name;

    @Expose
    @SerializedName("is_private")
    private boolean isPrivate;

    @Expose
    private String description;

    @Expose
    @SerializedName("fork_policy")
    private String forkPolicy;

    @Expose
    private String language;

    @Expose
    @SerializedName("has_issues")
    private boolean hasIssues;

    @Expose
    @SerializedName("has_wiki")
    private boolean hasWiki;


    public BitBucketRepository() {
        super();
    }

    public String getScm() {
        return scm;
    }

    public void setScm(String scm) {
        this.scm = scm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForkPolicy() {
        return forkPolicy;
    }

    public void setForkPolicy(String forkPolicy) {
        this.forkPolicy = forkPolicy;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean hasIssues() {
        return hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean hasWiki() {
        return hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }
}
